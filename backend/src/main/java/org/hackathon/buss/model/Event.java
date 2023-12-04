package org.hackathon.buss.model;

import com.fasterxml.jackson.annotation.JsonView;
import jakarta.persistence.*;
import lombok.*;
import org.hackathon.buss.enums.EventType;
import org.hackathon.buss.util.view.DetailedInformation;
import org.hackathon.buss.util.view.NonDetailedInformation;

import java.io.Serializable;
import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "events")
@JsonView({DetailedInformation.class, NonDetailedInformation.class})
public class Event implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String description;

    private LocalDateTime dateTime;

    @Enumerated(EnumType.STRING)
    private EventType type;

    @ManyToOne
    @JsonView({NonDetailedInformation.class})
    private Bus bus;

    @ManyToOne
    @JsonView({NonDetailedInformation.class})
    private Driver driver;

    @ManyToOne
    @JsonView({NonDetailedInformation.class})
    private Dispatcher dispatcher;

    private boolean processed;
}
