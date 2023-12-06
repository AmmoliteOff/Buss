package org.hackathon.buss.model;

import com.fasterxml.jackson.annotation.JsonView;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
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

    private String title;

    private double latitude;
    private double longitude;

    private double buildingLatitude;
    private double buildingLongitude;

    @Override
    public boolean equals(Object stop){
        if(!(stop instanceof Stop c))
            return false;
        return c.getId().equals(id);
    }

}
