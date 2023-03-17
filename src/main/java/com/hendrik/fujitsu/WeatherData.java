package com.hendrik.fujitsu;


import jakarta.persistence.*;
import org.springframework.data.annotation.Id;

import java.time.LocalDateTime;
@Entity
public class WeatherData {

    @jakarta.persistence.Id
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String stationName;

    private String wmoCode;

    private Double airTemperature;

    private Double windSpeed;

    private String weatherPhenomenon;

    private Long observationTime;

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public String getStationName() {
        return stationName;
    }

    public String getWmoCode() {
        return wmoCode;
    }

    public Double getAirTemperature() {
        return airTemperature;
    }

    public Double getWindSpeed() {
        return windSpeed;
    }

    public String getWeatherPhenomenon() {
        return weatherPhenomenon;
    }

    public Long getObservationTime() {
        return observationTime;
    }

    public void setStationName(String stationName) {
        this.stationName = stationName;
    }

    public void setWmoCode(String wmoCode) {
        this.wmoCode = wmoCode;
    }

    public void setAirTemperature(double airTemperature) {
        this.airTemperature = airTemperature;
    }

    public void setWindSpeed(double windSpeed) {
        this.windSpeed = windSpeed;
    }

    public void setWeatherPhenomenon(String weatherPhenomenon) {
        this.weatherPhenomenon = weatherPhenomenon;
    }

    public void setObservationTimestamp(Long timestamp) {
        this.observationTime = timestamp;
    }

    // getters and setters
}
