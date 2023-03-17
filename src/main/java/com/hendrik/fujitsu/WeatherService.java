package com.hendrik.fujitsu;

import org.springframework.boot.CommandLineRunner;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import org.springframework.beans.factory.annotation.Autowired;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.StringReader;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;


@Service
public class WeatherService implements CommandLineRunner {
    private static final String WEATHER_DATA_URL = "https://www.ilmateenistus.ee/ilma_andmed/xml/observations.php";

    private static final String[] STATIONS = {"Tallinn-Harku", "Tartu-Tõravere", "Pärnu"};
    private final WeatherDataRepository weatherDataRepository;

    @Autowired
    public WeatherService(WeatherDataRepository weatherDataRepository) {
        this.weatherDataRepository = weatherDataRepository;
    }
    //This method is called when the application is started
    //It is used to fetch the weather data from the URL and save it to the database
    //It is also used to schedule the fetching of the weather data
    @Scheduled(cron = "0 15 * * * *")
    public void getWeatherData() throws Exception {
        RestTemplate restTemplate = new RestTemplate();
        String weatherDataXml = restTemplate.getForObject(WEATHER_DATA_URL, String.class);

            parseWeatherDataXml(weatherDataXml);
    }

    private void parseWeatherDataXml(String xml) throws Exception {
        Map<String, String> weatherDataMap = new HashMap<>();
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        InputSource inputSource = new InputSource(new StringReader(xml));
        Document document = builder.parse(inputSource);
        //counts how many weather stations are found
        //if the count reaches 3, the for loop will break
        int k = 0;
        for (int i = 1; i < 100;i++) {
            if(document.getElementsByTagName("name").item(i).getTextContent().equals("Tallinn-Harku") ||
                    document.getElementsByTagName("name").item(i).getTextContent().equals("Tartu-Tõravere") ||
                    document.getElementsByTagName("name").item(i).getTextContent().equals("Pärnu")) {
                k++;
                weatherDataMap.put("stationName", document.getElementsByTagName("name").item(i).getTextContent());
                weatherDataMap.put("wmoCode", document.getElementsByTagName("wmocode").item(i).getTextContent());
                weatherDataMap.put("airTemperature", document.getElementsByTagName("airtemperature").item(i).getTextContent());
                weatherDataMap.put("windSpeed", document.getElementsByTagName("windspeed").item(i).getTextContent());
                weatherDataMap.put("weatherPhenomenon", document.getElementsByTagName("phenomenon").item(i).getTextContent());
                weatherDataMap.put("observationTime", document.getElementsByTagName("observations").item(0).getAttributes().getNamedItem("timestamp").getTextContent());


                WeatherData weatherData = new WeatherData();
                weatherData.setStationName(weatherDataMap.get("stationName"));
                weatherData.setWmoCode(weatherDataMap.get("wmoCode"));
                weatherData.setAirTemperature(Double.parseDouble(weatherDataMap.get("airTemperature")));
                weatherData.setWindSpeed(Double.parseDouble(weatherDataMap.get("windSpeed")));
                weatherData.setWeatherPhenomenon(weatherDataMap.get("weatherPhenomenon"));
                weatherData.setObservationTimestamp(Long.parseLong(weatherDataMap.get("observationTime")));
                weatherDataRepository.save(weatherData);
            }
            if(k == 3) {
                break;
            }
        }
    }


    @Override
    public void run(String... args) throws Exception {
        getWeatherData();
    }
}