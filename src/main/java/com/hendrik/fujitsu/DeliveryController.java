package com.hendrik.fujitsu;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Objects;

@RestController
public class DeliveryController {

    @Autowired
    private WeatherDataRepository weatherDataRepository;
    //This method is used to calculate the delivery fee
    //It is also the REST endpoint
    @GetMapping("/delivery-fees")
    public ResponseEntity<String> getDeliveryFee(
            @RequestParam("city") String city,
            @RequestParam("vehicleType") String vehicleType) throws Exception {
        if(!Objects.equals(city, "Tallinn") && !Objects.equals(city, "Tartu") && !Objects.equals(city, "Pärnu")){
            return ResponseEntity.badRequest().body("City not found");
        }
        if(!Objects.equals(vehicleType, "Car") && !Objects.equals(vehicleType, "Scooter") && !Objects.equals(vehicleType, "Bike")){
            return ResponseEntity.badRequest().body("Vehicle type not found");
        }
        double fee = calculateRegionalFee(city, vehicleType) + calculateExtraFeeATEF(city, vehicleType)
                + calculateExtraFeeWSEF(city, vehicleType) + calculateExtraFeeWPEF(city, vehicleType);

        return ResponseEntity.ok(fee + " €");

    }
    //Calculates the regional fee
    //Params: city, vehicleType
    //Returns: regional fee based on city and vehicle type
    double calculateRegionalFee(String city, String vehicleType) {
        if (Objects.equals(city, "Tallinn")) {
            if (Objects.equals(vehicleType, "Car")) {
                return 4;
            } else if (Objects.equals(vehicleType, "Scooter")) {
                return 3.5;
            } else if (Objects.equals(vehicleType, "Bike")) {
                return 3;
            }
        }
        else if (Objects.equals(city, "Tartu")) {
            if (Objects.equals(vehicleType, "Car")) {
                return 3.5;
            } else if (Objects.equals(vehicleType, "Scooter")) {
                return 3;
            } else if (Objects.equals(vehicleType, "Bike")) {
                return 2.5;
            }
        }
        else if (Objects.equals(city, "Pärnu")) {
            if (Objects.equals(vehicleType, "Car")) {
                return 3;
            } else if (Objects.equals(vehicleType, "Scooter")) {
                return 2.5;
            } else if (Objects.equals(vehicleType, "Bike")) {
                return 2;
            }
        }
        return 0;
    }
    //Calculates the extra fee based on air temperature
    //Params: city, vehicleType
    //Returns: extra fee based on air temperature
    double calculateExtraFeeATEF(String city, String vehicleType){
        boolean scooterOrBike = Objects.equals(vehicleType, "Scooter") || Objects.equals(vehicleType, "Bike");
        if(Objects.equals(city,"Tallinn")){
            WeatherData weatherData = findWeatherData("Tallinn");
            if(scooterOrBike){
                if(weatherData.getAirTemperature() < -10){
                    return 1;
                }
                else if(0 > weatherData.getAirTemperature() && weatherData.getAirTemperature() > -10){
                    return 0.5;
                }
            }
        }
        if(Objects.equals(city,"Tartu")){
            WeatherData weatherData = findWeatherData("Tartu");
            if(scooterOrBike){
                if(weatherData.getAirTemperature() < -10){
                    return 1;
                }
                else if(1 > weatherData.getAirTemperature() && weatherData.getAirTemperature() > -10){
                    return 0.5;
                }
            }
        }
        if(Objects.equals(city,"Pärnu")){
            WeatherData weatherData = findWeatherData("Pärnu");
            if(scooterOrBike){
                if(weatherData.getAirTemperature() < -10){
                    return 1;
                }
                else if(1 > weatherData.getAirTemperature() && weatherData.getAirTemperature() > -10){
                    return 0.5;
                }
            }
        }
        return 0;
    }
    //Calculates the extra fee based on wind speed
    //Params: city, vehicleType
    //Returns: extra fee based on wind speed
    private double calculateExtraFeeWSEF(String city, String vehicleType) throws Exception {
        if(Objects.equals(city,"Tallinn")){
            WeatherData weatherData = findWeatherData("Tallinn");
            if(Objects.equals(vehicleType, "Bike")){
                if(weatherData.getWindSpeed() > 10 && weatherData.getWindSpeed() < 20){
                    return 0.5;
                }
                else if(weatherData.getWindSpeed() > 20){
                    throw new Exception("Usage of selected vehicle type is forbidden");
                }
            }
        }
        if(Objects.equals(city,"Tartu")){
            WeatherData weatherData = findWeatherData("Tartu");
            if(Objects.equals(vehicleType, "Bike")){
                if(weatherData.getWindSpeed() > 10 && weatherData.getWindSpeed() < 20){
                    return 0.5;
                }
                else if(weatherData.getWindSpeed() > 20){
                    throw new Exception("Usage of selected vehicle type is forbidden");
                }
            }
        }
        if(Objects.equals(city,"Pärnu")){
            WeatherData weatherData = findWeatherData("Pärnu");
            if(Objects.equals(vehicleType, "Bike")){
                if(weatherData.getWindSpeed() > 10 && weatherData.getWindSpeed() < 20){
                    return 0.5;
                }
                else if(weatherData.getWindSpeed() > 20){
                    throw new Exception("Usage of selected vehicle type is forbidden");
                }
            }
        }
        return 0;
    }
    //Calculates the extra fee based on weather phenomenon
    //Params: city, vehicleType
    //Returns: extra fee based on weather phenomenon
    private double calculateExtraFeeWPEF(String city, String vehicleType) throws Exception {
        boolean scooterOrBike = Objects.equals(vehicleType, "Scooter") || Objects.equals(vehicleType, "Bike");


        if(Objects.equals(city,"Tallinn")){
            WeatherData weatherData = findWeatherData("Tallinn");
            boolean relatedToSnow = weatherData.getWeatherPhenomenon().contains("snow") || weatherData.getWeatherPhenomenon().contains("Snow")
                    || weatherData.getWeatherPhenomenon().contains("sleet") || weatherData.getWeatherPhenomenon().contains("Sleet")
                    || weatherData.getWeatherPhenomenon().contains("snowfall") || weatherData.getWeatherPhenomenon().contains("Snowfall");
            boolean relatedToRain = weatherData.getWeatherPhenomenon().contains("rain") || weatherData.getWeatherPhenomenon().contains("Rain")
                    || weatherData.getWeatherPhenomenon().contains("shower") || weatherData.getWeatherPhenomenon().contains("Shower");
            boolean forbiddenWeather = weatherData.getWeatherPhenomenon().contains("Hail") || weatherData.getWeatherPhenomenon().contains("Glaze")
                    || weatherData.getWeatherPhenomenon().contains("Thunder") || weatherData.getWeatherPhenomenon().contains("Thunderstorm");
            if(scooterOrBike){
                if(relatedToSnow){
                    return 1;
                }
                else if(relatedToRain){
                    return 0.5;
                }
                else if(forbiddenWeather){
                    throw new Exception("Usage of selected vehicle type is forbidden");
                }
            }
        }
        if(Objects.equals(city,"Tartu")){
            WeatherData weatherData = findWeatherData("Tartu");
            boolean relatedToSnow = weatherData.getWeatherPhenomenon().contains("snow") || weatherData.getWeatherPhenomenon().contains("Snow")
                    || weatherData.getWeatherPhenomenon().contains("sleet") || weatherData.getWeatherPhenomenon().contains("Sleet")
                    || weatherData.getWeatherPhenomenon().contains("snowfall") || weatherData.getWeatherPhenomenon().contains("Snowfall");
            boolean relatedToRain = weatherData.getWeatherPhenomenon().contains("rain") || weatherData.getWeatherPhenomenon().contains("Rain")
                    || weatherData.getWeatherPhenomenon().contains("shower") || weatherData.getWeatherPhenomenon().contains("Shower");
            boolean forbiddenWeather = weatherData.getWeatherPhenomenon().contains("Hail") || weatherData.getWeatherPhenomenon().contains("Glaze")
                    || weatherData.getWeatherPhenomenon().contains("Thunder") || weatherData.getWeatherPhenomenon().contains("Thunderstorm");
            if(scooterOrBike){
                if(relatedToSnow){
                    return 1;
                }
                else if(relatedToRain){
                    return 0.5;
                }
                else if(forbiddenWeather){
                    throw new Exception("Usage of selected vehicle type is forbidden");
                }
            }
        }
        if(Objects.equals(city,"Pärnu")){
            WeatherData weatherData = findWeatherData("Pärnu");
            boolean relatedToSnow = weatherData.getWeatherPhenomenon().contains("snow") || weatherData.getWeatherPhenomenon().contains("Snow")
                    || weatherData.getWeatherPhenomenon().contains("sleet") || weatherData.getWeatherPhenomenon().contains("Sleet")
                    || weatherData.getWeatherPhenomenon().contains("snowfall") || weatherData.getWeatherPhenomenon().contains("Snowfall");
            boolean relatedToRain = weatherData.getWeatherPhenomenon().contains("rain") || weatherData.getWeatherPhenomenon().contains("Rain")
                    || weatherData.getWeatherPhenomenon().contains("shower") || weatherData.getWeatherPhenomenon().contains("Shower");
            boolean forbiddenWeather = weatherData.getWeatherPhenomenon().contains("Hail") || weatherData.getWeatherPhenomenon().contains("Glaze")
                    || weatherData.getWeatherPhenomenon().contains("Thunder") || weatherData.getWeatherPhenomenon().contains("Thunderstorm");
            if(scooterOrBike){
                if(relatedToSnow){
                    return 1;
                }
                else if(relatedToRain){
                    return 0.5;
                }
                else if(forbiddenWeather){
                    throw new Exception("Usage of selected vehicle type is forbidden");
                }
            }
        }
        return 0;

    }
    //Finds the latest weather data for a city
    //Params: city
    //Returns: WeatherData
    private WeatherData findWeatherData(String city){
        String stationName;
        if(Objects.equals(city,"Tallinn")){
            stationName = "Tallinn-Harku";
        }
        else if(Objects.equals(city,"Tartu")){
            stationName = "Tartu-Tõravere";
        }
        else if(Objects.equals(city,"Pärnu")){
            stationName = "Pärnu";
        }
        else{
            throw new IllegalArgumentException("City not found");
        }
        List<WeatherData> weatherDataList= weatherDataRepository.findAllByStationName(stationName);
        Long max = weatherDataList.get(0).getObservationTime();
        int k = 0;
        for (int i = 0; i < weatherDataList.size(); i++) {
            if(weatherDataList.get(i).getObservationTime() > max){
                max = weatherDataList.get(i).getObservationTime();
                k = i;
            }
        }
        return weatherDataList.get(k);
    }
}

