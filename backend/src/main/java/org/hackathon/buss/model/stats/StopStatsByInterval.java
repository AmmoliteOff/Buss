package org.hackathon.buss.model.stats;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonView;
import jakarta.persistence.*;
import lombok.Data;
import org.hackathon.buss.util.view.DetailedInformation;
import org.hackathon.buss.util.view.NonDetailedInformation;

@Entity
@Data
@JsonView({DetailedInformation.class})
public class StopStatsByInterval {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long stopStatsByIntervalId;

    @ManyToOne
    @JsonIgnore
    private StopStatsByDay stopStatsByDay;

    private int peopleCount;
}
