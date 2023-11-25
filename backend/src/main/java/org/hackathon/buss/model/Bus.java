package org.hackathon.buss.model;

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
@Table(name = "buses")
public class Bus {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private double charge;

    @OneToOne
    private Route route;

    @OneToOne
    private User busDriver;

    @OneToOne
    private Schedule schedule;

    private int posX;

    private int posY;

    private LocalDateTime nextCharge;
}
