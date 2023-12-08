package org.hackathon.buss.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonView;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hackathon.buss.util.view.NonDetailedInformation;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "schedules")
public class Schedule {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long scheduleId;

    @OneToOne
    @JsonView(NonDetailedInformation.class)
    private Bus bus;

    @ManyToOne
    @JsonIgnore
    private Route route;

    private LocalDateTime startTime;
    private LocalDateTime endTime;
}
