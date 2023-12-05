package org.hackathon.buss.model.stats;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonView;
import jakarta.persistence.*;
import lombok.Data;
import org.hackathon.buss.model.Stop;
import org.hackathon.buss.util.view.DetailedInformation;
import org.hackathon.buss.util.view.NonDetailedInformation;

import java.util.List;

@Entity
@Data
@JsonView({DetailedInformation.class})
public class StopStatsByDay {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long stopStatsId;

    @ManyToOne
    @JsonIgnore
    private Stop stop;

    @OneToMany(mappedBy = "stopStatsByDay", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<StopStatsByInterval> stopStatsByIntervalList;
}
