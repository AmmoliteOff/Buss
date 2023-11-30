package org.hackathon.buss.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hackathon.buss.model.stats.StopPeopleStats;

import java.util.HashMap;
import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "stop")
public class Stop {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    private int posX;

    private int posY;

    @JsonIgnore
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "stop")
    private Map<Integer, StopPeopleStats> peopleStatsMap = new HashMap<>();
}
