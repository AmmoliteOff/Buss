package org.hackathon.buss.model;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;

import java.util.List;

@Entity
@DiscriminatorValue("DRIVER")
public class Driver extends User {

    @OneToMany(mappedBy = "user")
    private List<Message> messages;
}
