package org.hackathon.buss.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hackathon.buss.enums.DriverStatus;


@EqualsAndHashCode(callSuper = true)
@Data
@Entity
@DiscriminatorValue("DRIVER")
public class Driver extends User {

    public Driver(User user) {
        this.setId(user.getId());
        this.setName(user.getName());
        this.setId(user.getId());
        this.setSurname(user.getSurname());
        this.setRole(user.getRole());
        this.setPassword(user.getPassword());
        this.setUsername(user.getUsername());
    }

    @Enumerated(EnumType.STRING)
    private DriverStatus driverStatus;

    @OneToOne(mappedBy = "driver", cascade = CascadeType.ALL)
    private Chat chat;

    public Driver() {

    }
}
