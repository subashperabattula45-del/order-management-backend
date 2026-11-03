package com.subash.ordermanagement.user.controller;

import com.subash.ordermanagement.user.dto.UserRequest;
import com.subash.ordermanagement.user.dto.UserResponse;
import com.subash.ordermanagement.user.dto.UserUpdateRequest;
import com.subash.ordermanagement.user.service.UserService;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    public ResponseEntity<UserResponse> createUser(@Valid @RequestBody UserRequest request) {

        UserResponse savedUser = userService.createUser(request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(savedUser);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserResponse> getUserById(@PathVariable  Long id){

        UserResponse response = userService.getUserById(id);

        return  ResponseEntity.ok(response);

    }

    @GetMapping("/{all users}")
    public ResponseEntity<List<UserResponse>> getAllUsers(){

        List<UserResponse> users = userService.getAllUsers();

        return ResponseEntity
                  .status(HttpStatus.OK)
                  .body(users);
    }

    @PutMapping("/{id}")
public ResponseEntity<UserResponse> updateUser(
        @PathVariable Long id,
        @Valid @RequestBody UserUpdateRequest request) {

    UserResponse response = userService.updateUser(id, request);

    return ResponseEntity.ok(response);
}

@DeleteMapping("/{id}")
public ResponseEntity<Void> deleteUser(@PathVariable Long id){

    userService.deleteUser(id);

    return ResponseEntity.noContent().build();
}
}