package com.subash.ordermanagement.order.service;

import com.subash.ordermanagement.order.dto.OrderItemRequest;
import com.subash.ordermanagement.order.dto.OrderRequest;
import com.subash.ordermanagement.order.dto.OrderResponse;
import com.subash.ordermanagement.order.dto.OrderStatusUpdateRequest;
import com.subash.ordermanagement.order.repository.OrderRepository;
import com.subash.ordermanagement.product.repository.ProductRepository;
import com.subash.ordermanagement.promo.entity.DiscountType;
import com.subash.ordermanagement.promo.entity.PromoCode;
import com.subash.ordermanagement.promo.repository.PromoRepository;
import com.subash.ordermanagement.user.repository.UserRepository;
import com.subash.ordermanagement.user.entity.User;
import com.subash.ordermanagement.order.entity.Order;
import com.subash.ordermanagement.order.entity.OrderItem;
import com.subash.ordermanagement.order.entity.OrderStatus;
import com.subash.ordermanagement.order.dto.OrderItemResponse;

import com.subash.ordermanagement.product.entity.Product;
import org.springframework.transaction.annotation.Transactional;
import java.util.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.subash.ordermanagement.exception.*;

public class OrderService {

        private final OrderRepository orderRepository;
        private ProductRepository productRepository;
        private UserRepository userRepository;
        private final PromoRepository promoRepository;

        public OrderService(
                        OrderRepository orderRepository,
                        ProductRepository productRepository,
                        UserRepository userRepository,
                        PromoRepository promoRepository) {

                this.orderRepository = orderRepository;
                this.productRepository = productRepository;
                this.userRepository = userRepository;
                this.promoRepository = promoRepository;
        }

        @Transactional
        public OrderResponse createOrder(OrderRequest request) {

                User user = userRepository.findById(request.getUserId())
                                .orElseThrow(() -> new UserNotFoundException(
                                                "user not found with id " + request.getUserId()));

                Order order = new Order();

                order.setUser(user);
                order.setStatus(OrderStatus.PLACED);
                order.setCreatedAt(LocalDateTime.now());
                order.setTotalAmount(BigDecimal.ZERO);

                BigDecimal subTotal = BigDecimal.ZERO;

                for (OrderItemRequest itemRequest : request.getItems()) {

                        Product product = productRepository.findById(itemRequest.getProductId()).orElseThrow(
                                        () -> new ProductNotFoundException(
                                                        "product not found with id" + itemRequest.getProductId()));

                        if (product.getStock() < itemRequest.getQuantity()) {
                                throw new InsufficientStockException(
                                                "Insufficient stock for the prodcut" + product.getName());
                        }

                        product.setStock(product.getStock() - itemRequest.getQuantity());

                        OrderItem orderItem = new OrderItem();

                        orderItem.setOrder(order);
                        orderItem.setProduct(product);
                        orderItem.setQuantity(itemRequest.getQuantity());
                        orderItem.setPrice(product.getPrice());

                        order.getItems().add(orderItem);

                        BigDecimal total = product.getPrice().multiply(BigDecimal.valueOf(itemRequest.getQuantity()));

                        subTotal = subTotal.add(total);
                }

                order.setSubtotal(subTotal);
                order.setDiscountAmount(BigDecimal.ZERO);
                order.setTotalAmount(subTotal);
                order.setPromoCode(null);

                if (request.getPromoCode() != null && !request.getPromoCode().isBlank()) {

                        PromoCode promo = promoRepository.findByCode(request.getPromoCode())
                                        .orElseThrow(() -> new PromoNotFoundException(
                                                        "Promo code not found: " + request.getPromoCode()));

                        if (!promo.getActive()) {
                                throw new InvalidPromoException("Promo code is inactive");
                        }

                        LocalDateTime now = LocalDateTime.now();

                        if (now.isBefore(promo.getValidFrom())) {
                                throw new InvalidPromoException("Promo code is not active yet");
                        }

                        if (now.isAfter(promo.getValidTo())) {
                                throw new InvalidPromoException("Promo code has expired");
                        }

                        if (promo.getCurrentUses() >= promo.getMaxUses()) {
                                throw new InvalidPromoException("Promo code usage limit reached");
                        }

                        if (subTotal.compareTo(promo.getMinimumOrderAmount()) < 0) {
                                throw new InvalidPromoException(
                                                "Minimum order amount is " + promo.getMinimumOrderAmount());
                        }

                        BigDecimal discountAmount;

                        if (promo.getDiscountType() == DiscountType.PERCENTAGE) {

                                discountAmount = subTotal
                                                .multiply(promo.getDiscountValue())
                                                .divide(BigDecimal.valueOf(100));

                        } else {

                                discountAmount = promo.getDiscountValue();
                        }

                        if (discountAmount.compareTo(subTotal) > 0) {
                                discountAmount = subTotal;
                        }
                        BigDecimal finalAmount = subTotal.subtract(discountAmount);

                        order.setDiscountAmount(discountAmount);
                        order.setTotalAmount(finalAmount);
                        order.setPromoCode(promo.getCode());

                        promo.setCurrentUses(promo.getCurrentUses() + 1);
                }

                Order savedOrder = orderRepository.save(order);

                return mapToResponse(savedOrder);
        }

        public OrderResponse getOrderById(
                        Long id,
                        Long loggedInUserId,
                        String role) {

                Order order = orderRepository.findById(id)
                                .orElseThrow(() -> new OrderNotFoundException(
                                                "Order not found with id: " + id));

                if (!role.equals("ADMIN") &&
                                !order.getUser().getId().equals(loggedInUserId)) {

                        throw new InvalidOrderAccessException(
                                        "You cannot access this order");
                }

                return mapToResponse(order);
        }

        public List<OrderResponse> getAllOrders() {

                return orderRepository.findAll()
                                .stream()
                                .map(this::mapToResponse)
                                .toList();
        }

        public List<OrderResponse> getOrdersByUserId(Long userId) {

                userRepository.findById(userId)
                                .orElseThrow(() -> new UserNotFoundException(
                                                "User not found with id: " + userId));

                return orderRepository.findByUserId(userId)
                                .stream()
                                .map(this::mapToResponse)
                                .toList();
        }

        private OrderResponse mapToResponse(Order order) {

                List<OrderItemResponse> itemResponses = order.getItems()
                                .stream()
                                .map(item -> new OrderItemResponse(
                                                item.getProduct().getId(),
                                                item.getProduct().getName(),
                                                item.getQuantity(),
                                                item.getPrice()))
                                .toList();

                return new OrderResponse(
                                order.getId(),
                                order.getUser().getId(),
                                order.getStatus(),
                                order.getSubtotal(),
                                order.getDiscountAmount(),
                                order.getTotalAmount(),
                                order.getPromoCode(),
                                order.getCreatedAt(),
                                itemResponses);
        }

        public OrderResponse updateOrderStatus(Long id, OrderStatusUpdateRequest request) {

                Order order = orderRepository.findById(id)
                                .orElseThrow(() -> new OrderNotFoundException("order not found with id " + id));

                OrderStatus currentStatus = order.getStatus();
                OrderStatus newStatus = request.getStatus();

                if (currentStatus == OrderStatus.CANCELLED) {
                        throw new InvalidOrderStatusException(
                                        "Cancelled order status cannot be changed");
                }

                if (currentStatus == OrderStatus.CONFIRMED
                                && newStatus == OrderStatus.PLACED) {

                        throw new InvalidOrderStatusException(
                                        "Confirmed order cannot move back to PLACED");
                }

                if (newStatus == OrderStatus.CANCELLED) {

                        for (OrderItem item : order.getItems()) {

                                Product product = item.getProduct();

                                product.setStock(
                                                product.getStock() + item.getQuantity());
                        }
                }

                order.setStatus(newStatus);

                Order updateOrder = orderRepository.save(order);

                return mapToResponse(updateOrder);

        }

        @Transactional
        public OrderResponse cancelOrder(
                        Long orderId,
                        Long loggedInUserId) {

                Order order = orderRepository.findById(orderId)
                                .orElseThrow(() -> new OrderNotFoundException(
                                                "Order not found: " + orderId));

                if (!order.getUser().getId().equals(loggedInUserId)) {
                        throw new InvalidOrderAccessException(
                                        "You cannot cancel this order");
                }

                if (order.getStatus() == OrderStatus.CANCELLED) {
                        throw new InvalidOrderStatusException(
                                        "Order is already cancelled");
                }

                if (order.getStatus() != OrderStatus.PLACED &&
                                order.getStatus() != OrderStatus.CONFIRMED) {

                        throw new InvalidOrderStatusException(
                                        "Order cannot be cancelled from status: "
                                                        + order.getStatus());
                }

                for (OrderItem item : order.getItems()) {

                        Product product = item.getProduct();

                        product.setStock(
                                        product.getStock() + item.getQuantity());
                }

                order.setStatus(OrderStatus.CANCELLED);

                Order savedOrder = orderRepository.save(order);

                return mapToResponse(savedOrder);
        }

}
