package org.hackathon.buss.model;

import com.fasterxml.jackson.annotation.JsonView;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hackathon.buss.model.stats.StopStatsByDay;
import org.hackathon.buss.util.view.DetailedInformation;
import org.hackathon.buss.util.view.NonDetailedInformation;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "stops")
@JsonView({NonDetailedInformation.class, DetailedInformation.class})
public class Stop {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String title;

    @OneToMany(mappedBy = "stop", cascade = CascadeType.ALL)
    @JsonView({DetailedInformation.class})
    List<StopStatsByDay> statsByWeek;

    private double latitude;
    private double longitude;

    @Override
    public boolean equals(Object stop){
        if(!(stop instanceof Stop c))
            return false;
        return c.getId().equals(id);
    }

}
