package org.hackathon.buss.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hackathon.buss.enums.WeatherCondition;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class Weather {

    private WeatherCondition weatherCondition;

    private int temp;
}
