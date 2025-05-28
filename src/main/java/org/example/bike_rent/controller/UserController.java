package org.example.bike_rent.controller;

import java.util.List;

import org.example.bike_rent.entity.user.User;
import org.example.bike_rent.repository.UserRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PathVariable;
import org.example.bike_rent.factory.UserFactory;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "Users", description = "Endpoints for managing users")
@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserController(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Operation(summary = "Get all users", description = "Returns a list of all users. Admin only.")
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @Operation(summary = "Get user by ID", description = "Returns details of a user by their ID. Admin only.")
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/{id}")
    public ResponseEntity<User> getUserById(@PathVariable Long id) {
        return userRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    static class CreateUserRequest {
        public String email;
        public String password;
        public String user_type;
        public String firstName;
        public String lastName;
        public String phoneNumber;
        public String workerCode;
    }

    @Operation(summary = "Create a new user", description = "Creates a new user (admin or customer). Admin only.")
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<?> createUser(@RequestBody CreateUserRequest request) {
        if (userRepository.existsByEmail(request.email)) {
            return ResponseEntity.badRequest().body("Email already exists");
        }
        User newUser = UserFactory.createUser(request.user_type);
        newUser.setEmail(request.email);
        newUser.setPassword(passwordEncoder.encode(request.password));
        newUser.setFirstName(request.firstName);
        newUser.setLastName(request.lastName);
        if (newUser instanceof org.example.bike_rent.entity.user.Customer customer) {
            customer.setPhoneNumber(request.phoneNumber);
            customer.setActive(true);
        }
        if (newUser instanceof org.example.bike_rent.entity.user.Admin admin) {
            admin.setWorkerCode(request.workerCode);
        }
        return ResponseEntity.ok(userRepository.save(newUser));
    }
}


