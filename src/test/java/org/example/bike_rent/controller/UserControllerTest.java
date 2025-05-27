package org.example.bike_rent.controller;

import org.example.bike_rent.entity.user.User;
import org.example.bike_rent.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserControllerTest {

    private UserRepository userRepository;
    private PasswordEncoder passwordEncoder;
    private UserController userController;

    @BeforeEach
    void setUp() {
        userRepository = Mockito.mock(UserRepository.class);
        passwordEncoder = Mockito.mock(PasswordEncoder.class);
        userController = new UserController(userRepository, passwordEncoder);
    }

    @Test
    void testGetAllUsers() {
        User user1 = new User();
        user1.setId(1);
        user1.setEmail("john@example.com");
        user1.setPassword("pass1");
        user1.setFirstName("John");
        user1.setLastName("Doe");

        User user2 = new User();
        user2.setId(2);
        user2.setEmail("jane@example.com");
        user2.setPassword("pass2");
        user2.setFirstName("Jane");
        user2.setLastName("Smith");

        when(userRepository.findAll()).thenReturn(Arrays.asList(user1, user2));

        List<User> users = userController.getAllUsers();

        assertNotNull(users);
        assertEquals(2, users.size());
        assertEquals("john@example.com", users.get(0).getEmail());
        assertEquals("John", users.get(0).getFirstName());
        assertEquals("Doe", users.get(0).getLastName());

        verify(userRepository, times(1)).findAll();
    }

    @Test
    void testGetUserByIdExists() {
        User user = new User();
        user.setId(1);
        user.setEmail("john@example.com");
        user.setPassword("pass");
        user.setFirstName("John");
        user.setLastName("Doe");

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        ResponseEntity<User> response = userController.getUserById(1L);
        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        assertNotNull(response.getBody());
        assertEquals("john@example.com", response.getBody().getEmail());
        assertEquals("John", response.getBody().getFirstName());
        assertEquals("Doe", response.getBody().getLastName());

        verify(userRepository, times(1)).findById(1L);
    }

    @Test
    void testGetUserByIdNotFound() {
        when(userRepository.findById(100L)).thenReturn(Optional.empty());

        ResponseEntity<User> response = userController.getUserById(100L);
        assertNotNull(response);
        assertEquals(404, response.getStatusCodeValue());

        verify(userRepository, times(1)).findById(100L);
    }

    @Test
    void testCreateUserSuccess() {
        UserController.CreateUserRequest request = new UserController.CreateUserRequest();
        request.email = "newuser@example.com";
        request.password = "plainpass";
        request.user_type = "CUSTOMER";

        when(userRepository.existsByEmail(request.email)).thenReturn(false);
        when(passwordEncoder.encode(request.password)).thenReturn("encodedpass");

        User savedUser = new User();
        savedUser.setId(1);
        savedUser.setEmail(request.email);
        savedUser.setPassword("encodedpass");

        when(userRepository.save(any(User.class))).thenReturn(savedUser);

        ResponseEntity<?> response = userController.createUser(request);

        assertEquals(200, response.getStatusCodeValue());
        assertTrue(response.getBody() instanceof User);
        User resultUser = (User) response.getBody();
        assertEquals("newuser@example.com", resultUser.getEmail());
        assertEquals("encodedpass", resultUser.getPassword());

        verify(userRepository, times(1)).existsByEmail(request.email);
        verify(passwordEncoder, times(1)).encode(request.password);
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    void testCreateUserEmailExists() {
        UserController.CreateUserRequest request = new UserController.CreateUserRequest();
        request.email = "existing@example.com";
        request.password = "pass";
        request.user_type = "CUSTOMER";

        when(userRepository.existsByEmail(request.email)).thenReturn(true);

        ResponseEntity<?> response = userController.createUser(request);

        assertEquals(400, response.getStatusCodeValue());
        assertEquals("Email already exists", response.getBody());

        verify(userRepository, times(1)).existsByEmail(request.email);
        verify(userRepository, never()).save(any(User.class));
    }
}

