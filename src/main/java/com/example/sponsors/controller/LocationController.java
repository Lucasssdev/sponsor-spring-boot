package com.example.sponsors.controller;

import com.example.sponsors.model.Location;
import com.example.sponsors.service.GeocodingService;
import com.example.sponsors.service.LocationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;
@RestController
@RequestMapping("api/locations")
public class LocationController {
    @Autowired
    private LocationService locationService;
    @Autowired
    private GeocodingService geocodingService;

    public LocationController(LocationService locationService) {
        this.locationService = locationService;
    }

    @PostMapping
    public Location createLocation(@RequestBody Location location) {
        return locationService.saveLocation(location);
    }

    @GetMapping("/{id}")
    public Optional<Location> getLocation(@PathVariable Long id) {
        return locationService.getLocationById(id);
    }

    @PostMapping("/geocode")
    public Location getLocationFromAddress(@RequestParam String address) {
        Location location = geocodingService.getCoordinates(address);
        if (location == null) {
            throw new RuntimeException("Endereço inválido");
        }
        return locationService.saveLocation(location);
    }
}
