package org.example.bike_rent.security;

import org.example.bike_rent.entity.user.User;
import org.example.bike_rent.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CustomUserDetailsServiceTest {
    private UserRepository userRepository;
    private CustomUserDetailsService customUserDetailsService;
    @jakarta.persistence.DiscriminatorValue("ADMIN")
    static class AdminUser extends User {}

    @BeforeEach
    void setUp() {
        userRepository = Mockito.mock(UserRepository.class);
        customUserDetailsService = new CustomUserDetailsService(userRepository);
    }

    @Test
    void testLoadUserByUsername_AdminRole() {
        AdminUser user = new AdminUser();
        user.setEmail("admin@example.com");
        user.setPassword("pass");
        when(userRepository.findByEmail("admin@example.com")).thenReturn(Optional.of(user));
        UserDetails details = customUserDetailsService.loadUserByUsername("admin@example.com");
        assertEquals("admin@example.com", details.getUsername());
        assertEquals("pass", details.getPassword());
        assertTrue(details.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN")));
    }

    @Test
    void testLoadUserByUsername_CustomerRoleFallback() {
        User user = new User();
        user.setEmail("customer@example.com");
        user.setPassword("pass");
        when(userRepository.findByEmail("customer@example.com")).thenReturn(Optional.of(user));
        UserDetails details = customUserDetailsService.loadUserByUsername("customer@example.com");
        assertEquals("customer@example.com", details.getUsername());
        assertEquals("pass", details.getPassword());
        assertTrue(details.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_CUSTOMER")));
    }

    @Test
    void testLoadUserByUsername_UserNotFound() {
        when(userRepository.findByEmail("nouser@example.com")).thenReturn(Optional.empty());
        assertThrows(UsernameNotFoundException.class, () -> customUserDetailsService.loadUserByUsername("nouser@example.com"));
    }
}

