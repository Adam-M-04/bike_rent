package org.example.bike_rent.security;

import org.example.bike_rent.entity.user.User;
import org.example.bike_rent.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Collections;

@Service
public class CustomUserDetailsService implements UserDetailsService {
    private final UserRepository userRepository;

    @Autowired
    public CustomUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        String role;
        try {
            Class<?> userClass = user.getClass();
            java.lang.annotation.Annotation annotation = userClass.getAnnotation((Class<? extends java.lang.annotation.Annotation>) Class.forName("jakarta.persistence.DiscriminatorValue"));
            if (annotation != null) {
                String value = (String) annotation.annotationType().getMethod("value").invoke(annotation);
                role = "ROLE_" + value;
            } else {
                role = "ROLE_CUSTOMER";
            }
        } catch (Exception e) {
            role = "ROLE_CUSTOMER";
        }
        Collection<? extends GrantedAuthority> authorities = Collections.singletonList(new SimpleGrantedAuthority(role));
        return new org.springframework.security.core.userdetails.User(user.getEmail(), user.getPassword(), authorities);
    }
}

