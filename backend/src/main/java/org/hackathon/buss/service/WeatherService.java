package org.hackathon.buss.service;

import org.hackathon.buss.dto.WeatherDTO;
import org.hackathon.buss.model.Weather;
import org.springframework.http.*;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

@Service
public class WeatherService {

    private Weather weather;

    @Scheduled(fixedDelay = 1000 * 60 * 30)
    private void updateWeather() {
        URI uri = UriComponentsBuilder.fromUriString("https://api.weather.yandex.ru/v2/informers")
                .queryParam("lat", 51.660283)
                .queryParam("lon", 39.199128)
                .build()
                .toUri();

        HttpHeaders headers = new HttpHeaders();
        headers.set("X-Yandex-API-Key", "1f22067a-00f4-4220-a62a-e75dfda1475b");
        headers.setContentType(MediaType.APPLICATION_JSON);

        RequestEntity<Void> requestEntity = new RequestEntity<>(headers, HttpMethod.GET, uri);
        WeatherDTO weatherDTO = new RestTemplate().exchange(requestEntity, WeatherDTO.class).getBody();
        weather = WeatherDTO.toWeather(weatherDTO);
        System.out.println(getCurrentWeather());
    }

    public Weather getCurrentWeather() {
        return weather;
    }
}
