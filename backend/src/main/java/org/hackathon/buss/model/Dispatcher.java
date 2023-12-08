package org.hackathon.buss.model;

import com.fasterxml.jackson.annotation.JsonView;
import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.hackathon.buss.util.view.DetailedInformation;


import java.util.List;


@EqualsAndHashCode(callSuper = true)
@Entity
@Getter
@Setter
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

    @OneToMany(mappedBy = "dispatcher", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JsonView({DetailedInformation.class})
    private List<Event> events;

    @OneToMany(mappedBy = "dispatcher",fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private List<Chat> chat;

    public Dispatcher() {

    }
}
