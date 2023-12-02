package org.hackathon.buss.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hackathon.buss.enums.BusStatus;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "buses")
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
    private User busDriver;

    @OneToOne
    @JsonIgnore
    private Schedule schedule;

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
