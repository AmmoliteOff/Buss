package org.hackathon.buss.dto;

import lombok.Data;


@Data
public class WeatherDTO {

    private FactWeather fact;

    @Data
    public static class FactWeather {

        private int temp;

        private String condition;
    }
}
