package org.example.bike_rent.entity.user;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

@Entity
@DiscriminatorValue("CUSTOMER")
public class Customer extends User {
    public Customer() {
        super();
    }
}

