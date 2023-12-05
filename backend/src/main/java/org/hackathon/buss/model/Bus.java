package org.hackathon.buss.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonView;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hackathon.buss.enums.BusStatus;
import org.hackathon.buss.util.view.DetailedInformation;
import org.hackathon.buss.util.view.NonDetailedInformation;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "buses")
@JsonView({NonDetailedInformation.class, DetailedInformation.class})
public class Bus {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated
    private BusStatus status;

    private double charge;

    @ManyToOne
    @JsonIgnore
    private Route route;

    @OneToOne
    @JsonIgnore
    private Driver busDriver;

    @OneToOne
    @JsonIgnore
    private Schedule schedule;

    @OneToMany(mappedBy = "bus", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<RoadStops> roadStops = new ArrayList<>();

    private double latitude;
    private double longitude;

    private LocalDateTime nextCharge;

    @Override
    public boolean equals(Object other){
        if(!(other instanceof Bus c))
            return false;
        return c.getId().equals(id);
    }
}
