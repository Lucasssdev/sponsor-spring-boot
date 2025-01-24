package com.example.sponsors.service;


import com.example.sponsors.model.Location;
import org.json.JSONObject;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class GeocodingService {

    private final String GOOGLE_API_KEY = "AIzaSyB8_nlhO6iJNytV_DZocoPNBKw8HMxgRtA";

    public Location getCoordinates(String address) {
        String url = "https://maps.googleapis.com/maps/api/geocode/json?address=" + address + "&key=" + GOOGLE_API_KEY;
        RestTemplate restTemplate = new RestTemplate();
        String response = restTemplate.getForObject(url, String.class);

        JSONObject json = new JSONObject(response);
        if (!json.getString("status").equals("OK")) {
            return null;
        }

        JSONObject locationJson = json.getJSONArray("results").getJSONObject(0)
                .getJSONObject("geometry").getJSONObject("location");

        return new Location(address, locationJson.getDouble("lat"), locationJson.getDouble("lng"));
    }

}
