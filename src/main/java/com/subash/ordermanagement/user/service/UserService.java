package com.subash.ordermanagement.user.service;

import com.subash.ordermanagement.exception.DuplicateEmailException;
import com.subash.ordermanagement.exception.UserNotFoundException;
import com.subash.ordermanagement.user.dto.UserRequest;
import com.subash.ordermanagement.user.dto.UserResponse;
import com.subash.ordermanagement.user.dto.UserUpdateRequest;
import com.subash.ordermanagement.user.entity.User;
import com.subash.ordermanagement.user.repository.UserRepository;
import com.subash.ordermanagement.user.entity.Role;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.*;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository,PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public UserResponse createUser(UserRequest request) {

        if(userRepository.existsByEmail(request.getEmail())){
            throw new DuplicateEmailException("email already exists");
        }

        User user = new User();

        user.setName(request.getName());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRole(Role.USER);

        User savedUser = userRepository.save(user);

        return new UserResponse(
            savedUser.getId(),
            savedUser.getName(),
            savedUser.getEmail()
        );
    }

    public UserResponse getUserById(Long id){

        User user = userRepository.findById(id)
        .orElseThrow(() -> new UserNotFoundException("user not found with id" + id));

        return new UserResponse(
            user.getId(),
            user.getName(),
            user.getEmail()
        );
    }

   public List<UserResponse> getAllUsers() {

    return userRepository.findAll()
            .stream()
            .map(user -> new UserResponse(
                    user.getId(),
                    user.getName(),
                    user.getEmail()
            ))
            .toList();
}

public UserResponse updateUser(Long id, UserUpdateRequest request) {

    User user = userRepository.findById(id)
            .orElseThrow(() ->
                    new UserNotFoundException("User not found with id: " + id)
            );

    Optional<User> existingUser =
            userRepository.findByEmail(request.getEmail());

    if (existingUser.isPresent()
            && !existingUser.get().getId().equals(id)) {

        throw new DuplicateEmailException("Email already registered");
    }

    user.setName(request.getName());
    user.setEmail(request.getEmail());

    User updatedUser = userRepository.save(user);

    return new UserResponse(
            updatedUser.getId(),
            updatedUser.getName(),
            updatedUser.getEmail()
    );
}

public void deleteUser(Long id){

    User user = userRepository.findById(id)
    .orElseThrow(() -> new UserNotFoundException("user not found with id" + id));

    userRepository.delete(user);

}
}