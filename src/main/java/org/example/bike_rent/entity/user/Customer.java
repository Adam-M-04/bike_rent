package org.example.bike_rent.entity.user;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@DiscriminatorValue("CUSTOMER")
public class Customer extends User {
    public Customer() {
        super();
    }

    private String phoneNumber;
    private boolean isActive = true;
}
