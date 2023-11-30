package org.hackathon.buss.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;


@Getter
@Setter
@Entity
@DiscriminatorValue("DRIVER")
public class Driver extends User {

    @OneToMany(mappedBy = "sender")
    private List<Message> messages;
}
