package org.hackathon.buss.model;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;


import java.util.List;


@Entity
@DiscriminatorValue("DISPATCHER")
public class Dispatcher extends User{

    @OneToMany()
    private List<Event> events;

    @OneToMany(mappedBy = "user")
    private List<Message> messages;
}
