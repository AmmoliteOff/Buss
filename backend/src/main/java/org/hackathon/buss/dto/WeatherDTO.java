package org.hackathon.buss.dto;

import lombok.Data;
import org.hackathon.buss.enums.WeatherCondition;
import org.hackathon.buss.model.Weather;

@Data
public class WeatherDTO {

    private FactWeather fact;
    public static Weather toWeather(WeatherDTO weatherDTO) {
        Weather weather = new Weather();
        String condition = weatherDTO.fact.getCondition();
        if(condition.contains("cl"))
            weather.setWeatherCondition(WeatherCondition.CLEAR);
        else if(condition.contains("thunderstorm"))
            weather.setWeatherCondition(WeatherCondition.THUNDERSTORM);
        else if(condition.contains("rain") || condition.contains("showers"))
            weather.setWeatherCondition(WeatherCondition.RAIN);
        else if(condition.contains("snow") || condition.contains("hail"))
            weather.setWeatherCondition(WeatherCondition.SNOW);
        else
            weather.setWeatherCondition(WeatherCondition.OVERCAST);

        weather.setTemp(weatherDTO.fact.getTemp());
        return weather;
    }
    @Data
    static class FactWeather {

        private int temp;

        private String condition;
    }
}
