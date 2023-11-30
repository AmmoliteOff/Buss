package org.hackathon.buss.model;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import lombok.Data;

import java.util.List;

@Entity
@Data
@DiscriminatorValue("DRIVER")
public class Driver extends User {

    @OneToMany(mappedBy = "sender")
    private List<Message> messages;
}
