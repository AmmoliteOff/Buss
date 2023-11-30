package org.hackathon.buss.model;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import lombok.Getter;
import lombok.Setter;


import java.util.List;


@Getter
@Setter
@Entity
@DiscriminatorValue("DISPATCHER")
public class Dispatcher extends User{

    @OneToMany(mappedBy = "dispatcher")
    private List<Event> events;

    @OneToMany(mappedBy = "sender")
    private List<Message> messages;
}
