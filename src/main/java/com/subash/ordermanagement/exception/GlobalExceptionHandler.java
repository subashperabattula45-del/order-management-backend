package com.subash.ordermanagement.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

        @ExceptionHandler(DuplicateEmailException.class)
        public ResponseEntity<Map<String, String>> handleDuplicateEmail(
                        DuplicateEmailException ex) {

                return ResponseEntity
                                .status(HttpStatus.CONFLICT)
                                .body(Map.of("message", ex.getMessage()));
        }

        @ExceptionHandler(MethodArgumentNotValidException.class)
        public ResponseEntity<Map<String, String>> handleValidationErrors(
                        MethodArgumentNotValidException ex) {

                Map<String, String> errors = new HashMap<>();

                ex.getBindingResult()
                                .getFieldErrors()
                                .forEach(error -> errors.put(
                                                error.getField(),
                                                error.getDefaultMessage()));

                return ResponseEntity
                                .status(HttpStatus.BAD_REQUEST)
                                .body(errors);
        }

        @ExceptionHandler(UserNotFoundException.class)
        public ResponseEntity<Map<String, String>> handleuserNotFoundException(
                        UserNotFoundException ex) {

                return ResponseEntity
                                .status(HttpStatus.NOT_FOUND)
                                .body(Map.of("message", ex.getMessage()));
        }

        @ExceptionHandler(ProductNotFoundException.class)
        public ResponseEntity<Map<String, String>> handleProductNotFoundException(
                        ProductNotFoundException ex) {

                return ResponseEntity
                                .status(HttpStatus.NOT_FOUND)
                                .body(Map.of("message", ex.getMessage()));
        }

        @ExceptionHandler(InsufficientStockException.class)
        public ResponseEntity<Map<String, String>> handleInsufficientStockException(
                        InsufficientStockException ex) {

                return ResponseEntity
                                .status(HttpStatus.BAD_REQUEST).body(Map.of("message", ex.getMessage()));
        }

        @ExceptionHandler(OrderNotFoundException.class)
        public ResponseEntity<Map<String, String>> handleOrderNotFound(
                        OrderNotFoundException ex) {

                return ResponseEntity
                                .status(HttpStatus.NOT_FOUND)
                                .body(Map.of("message", ex.getMessage()));
        }

        @ExceptionHandler(InvalidOrderStatusException.class)
        public ResponseEntity<Map<String, String>> handleInvalidOrderStatus(
                        InvalidOrderStatusException ex) {

                return ResponseEntity
                                .status(HttpStatus.BAD_REQUEST)
                                .body(Map.of("message", ex.getMessage()));
        }

        @ExceptionHandler(DuplicatePromoCodeException.class)
        public ResponseEntity<Map<String, String>> handleDuplicatePromoCode(
                        DuplicatePromoCodeException ex) {

                return ResponseEntity
                                .status(HttpStatus.CONFLICT)
                                .body(Map.of("message", ex.getMessage()));
        }

        @ExceptionHandler(PromoNotFoundException.class)
        public ResponseEntity<Map<String, String>> handlePromoNotFound(
                        PromoNotFoundException ex) {

                return ResponseEntity
                                .status(HttpStatus.NOT_FOUND)
                                .body(Map.of("message", ex.getMessage()));
        }

        @ExceptionHandler(InvalidPromoException.class)
        public ResponseEntity<Map<String, String>> handleInvalidPromo(
                        InvalidPromoException ex) {

                return ResponseEntity
                                .status(HttpStatus.BAD_REQUEST)
                                .body(Map.of("message", ex.getMessage()));
        }

        @ExceptionHandler(PaymentNotFoundException.class)
        public ResponseEntity<Map<String, String>> handlePaymentNotFound(
                        PaymentNotFoundException ex) {

                return ResponseEntity
                                .status(HttpStatus.NOT_FOUND)
                                .body(Map.of("message", ex.getMessage()));
        }

        @ExceptionHandler(InvalidPaymentStatusException.class)
        public ResponseEntity<Map<String, String>> handleInvalidPaymentStatus(
                        InvalidPaymentStatusException ex) {

                return ResponseEntity
                                .status(HttpStatus.BAD_REQUEST)
                                .body(Map.of("message", ex.getMessage()));
        }

        @ExceptionHandler(InvalidPaymentException.class)
        public ResponseEntity<Map<String, String>> handleInvalidPayment(
                        InvalidPaymentException ex) {

                return ResponseEntity
                                .status(HttpStatus.BAD_REQUEST)
                                .body(Map.of("message", ex.getMessage()));
        }

        @ExceptionHandler(InvalidOrderAccessException.class)
        public ResponseEntity<Map<String, String>> handleInvalidOrderAccess(
                        InvalidOrderAccessException ex) {

                return ResponseEntity
                                .status(HttpStatus.FORBIDDEN)
                                .body(Map.of("message", ex.getMessage()));
        }

        @ExceptionHandler(InvalidCredentialsException.class)
        public ResponseEntity<Map<String, String>> handleInvalidCredentials(
                        InvalidCredentialsException ex) {

                return ResponseEntity
                                .status(HttpStatus.UNAUTHORIZED)
                                .body(Map.of("message", ex.getMessage()));
        }
}