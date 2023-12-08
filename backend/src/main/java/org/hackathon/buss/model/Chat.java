package org.hackathon.buss.model;

import com.fasterxml.jackson.annotation.JsonView;
import jakarta.persistence.*;
import lombok.*;
import org.hackathon.buss.util.view.DetailedInformation;
import org.hackathon.buss.util.view.NonDetailedInformation;

import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "chats")
@JsonView({DetailedInformation.class, NonDetailedInformation.class})
public class Chat {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "dispatcher_id")
    @JsonView(DetailedInformation.class)
    private Dispatcher dispatcher;

    @OneToOne
    @JoinColumn(name = "driver_id")
    @JsonView(DetailedInformation.class)
    private Driver driver;

    @ManyToOne
    @JoinColumn(name = "supervisor_id")
    @JsonView(DetailedInformation.class)
    private Supervisor supervisor;

    @OneToMany(mappedBy = "chat", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private List<Message> messages = new ArrayList<>();

}