package org.hackathon.buss.model;

import com.fasterxml.jackson.annotation.JsonView;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hackathon.buss.enums.DriverStatus;
import org.hackathon.buss.util.view.DetailedInformation;

import java.util.List;

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

    @OneToMany(mappedBy = "sender", cascade = CascadeType.ALL)
    @JsonView({DetailedInformation.class})
    private List<Message> messages;

    @Enumerated(EnumType.STRING)
    private DriverStatus driverStatus;

    public Driver() {

    }
}
