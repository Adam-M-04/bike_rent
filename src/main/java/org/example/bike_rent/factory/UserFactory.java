package org.example.bike_rent.factory;

import org.example.bike_rent.entity.user.Admin;
import org.example.bike_rent.entity.user.Customer;
import org.example.bike_rent.entity.user.User;

public class UserFactory {
    public static User createUser(String userType) {
        if ("ADMIN".equalsIgnoreCase(userType)) {
            return new Admin();
        } else if ("CUSTOMER".equalsIgnoreCase(userType)) {
            return new Customer();
        } else {
            throw new IllegalArgumentException("Unknown user type: " + userType);
        }
    }
}

