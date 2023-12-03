package org.hackathon.buss.service;

import lombok.extern.slf4j.Slf4j;
import org.hackathon.buss.dto.WeatherDTO;
import org.hackathon.buss.enums.WeatherCondition;
import org.hackathon.buss.model.Weather;
import org.springframework.http.*;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;


@Slf4j
@Service
public class WeatherService {

    private Weather weather;

    @Scheduled(fixedDelay = 1000 * 60 * 30)
    private void updateWeather() {
        WeatherDTO weatherDTO = weatherRequest();
        weather = toWeather(weatherDTO);
        log.info(getCurrentWeather().toString());
    }

    private WeatherDTO weatherRequest() {
        URI uri = UriComponentsBuilder.fromUriString("https://api.weather.yandex.ru/v2/informers")
                .queryParam("lat", 51.660283)
                .queryParam("lon", 39.199128)
                .build()
                .toUri();

        HttpHeaders headers = new HttpHeaders();
        headers.set("X-Yandex-API-Key", "1f22067a-00f4-4220-a62a-e75dfda1475b");
        headers.setContentType(MediaType.APPLICATION_JSON);

        RequestEntity<Void> requestEntity = new RequestEntity<>(headers, HttpMethod.GET, uri);
        return new RestTemplate().exchange(requestEntity, WeatherDTO.class).getBody();
    }

    public Weather toWeather(WeatherDTO weatherDTO) {
        Weather weather = new Weather();
        String condition = weatherDTO.getFact().getCondition();
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

        weather.setTemp(weatherDTO.getFact().getTemp());
        return weather;
    }

    public Weather getCurrentWeather() {
        return weather;
    }
}
