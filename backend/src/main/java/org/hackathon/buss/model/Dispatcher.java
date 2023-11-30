package org.hackathon.buss.model;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import lombok.Data;


import java.util.List;


@Entity
@Data
@DiscriminatorValue("DISPATCHER")
public class Dispatcher extends User{

    @OneToMany(mappedBy = "dispatcher")
    private List<Event> events;

    @OneToMany(mappedBy = "sender")
    private List<Message> messages;
}
