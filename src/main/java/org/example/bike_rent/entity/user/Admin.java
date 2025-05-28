package org.example.bike_rent.entity.user;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@DiscriminatorValue("ADMIN")
public class Admin extends User {
    private String workerCode;

    public Admin() {
        super();
    }
}
