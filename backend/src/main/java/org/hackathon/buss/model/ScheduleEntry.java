package org.hackathon.buss.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "schedule_entries")
public class ScheduleEntry {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JsonIgnore
    private Stop stop;

    @ManyToOne
    @JsonIgnore
    private Schedule schedule;

    private LocalDateTime time;

    public ScheduleEntry(LocalDateTime time, Schedule schedule, Stop stop){
        this.stop = stop;
        //this.schedule = schedule;
        this.time = time;
    }
}