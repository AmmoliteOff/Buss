package org.hackathon.buss.model;

import com.fasterxml.jackson.annotation.JsonView;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hackathon.buss.util.view.DetailedInformation;
import org.hackathon.buss.util.view.NonDetailedInformation;

import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
@Entity
@DiscriminatorValue("SUPERVISOR")
@JsonView({DetailedInformation.class, NonDetailedInformation.class})
public class Supervisor extends User {

    public Supervisor(User user) {
        this.setId(user.getId());
        this.setName(user.getName());
        this.setId(user.getId());
        this.setSurname(user.getSurname());
        this.setRole(user.getRole());
        this.setPassword(user.getPassword());
        this.setUsername(user.getUsername());
    }



    @OneToMany(mappedBy = "dispatcher", cascade = CascadeType.ALL)
    @JsonView(DetailedInformation.class)

    private List<Chat> chat;

    public Supervisor() {

    }
}
