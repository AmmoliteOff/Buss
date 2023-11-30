package org.hackathon.buss.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.CascadeType;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import lombok.Data;
import lombok.EqualsAndHashCode;


import java.util.List;


@EqualsAndHashCode(callSuper = true)
@Entity
@Data
@DiscriminatorValue("DISPATCHER")
public class Dispatcher extends User{

    public Dispatcher(User user) {
        this.setId(user.getId());
        this.setName(user.getName());
        this.setId(user.getId());
        this.setSurname(user.getSurname());
        this.setRole(user.getRole());
        this.setPassword(user.getPassword());
        this.setUsername(user.getUsername());
    }

    @OneToMany(mappedBy = "dispatcher", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Event> events;

    @OneToMany(mappedBy = "sender", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Message> messages;

    public Dispatcher() {

    }
}
