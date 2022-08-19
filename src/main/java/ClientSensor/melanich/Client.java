package ClientSensor.melanich;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class Client {
    public static void main(String[] args) {
        final String urlToRegistration = "http://localhost:8080/sensors/registration";
        final String urlToSendMeasurement = "http://localhost:8080/measurements/add";
        final String sensorName = "sensor1";

        Map<String, Object> jsonToRegistration = new HashMap<String,Object>();
        jsonToRegistration.put("name", sensorName);
        sendJSON(urlToRegistration, jsonToRegistration);

        Double maxTemperature = 45.0;
        Random random = new Random();
        for (int i=0; i<50; i++) {
            sendJSON(urlToSendMeasurement, createMeasurementJSON(random.nextDouble()*maxTemperature, random.nextBoolean(), sensorName));
        }
    }
    private static void sendJSON(String url, Map<String,Object> json) {
        final RestTemplate restTemplate = new RestTemplate();

        final HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<Object> request = new HttpEntity<>(json,headers);

        try {
            restTemplate.postForObject(url, request, String.class);
            System.out.println("Данные отправлены на сервер");
        } catch (HttpClientErrorException e){
            System.out.println("Ошибка!");
            System.out.println(e.getMessage());
        }
    }
    private static Map<String,Object> createMeasurementJSON(double temperature, boolean raining, String sensorName) {
        Map<String, Object> json = new HashMap<>();
        json.put("value", temperature);
        json.put("raining", raining);
        json.put("sensor", Map.of("name", sensorName));

        return json;
    }
}
