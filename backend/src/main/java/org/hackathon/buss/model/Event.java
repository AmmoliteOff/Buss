package org.hackathon.buss.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hackathon.buss.enums.EventType;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "events")
public class Event {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String description;

    private LocalDateTime dateTime;

    @Enumerated(EnumType.STRING)
    private EventType type;

    @OneToOne
    private Bus bus;

    @ManyToOne
    private User driver;

    @ManyToOne
    private User dispatcher;

    private boolean processed;
}
