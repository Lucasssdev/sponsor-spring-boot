package com.example.sponsors.service;

import com.example.sponsors.model.Location;
import com.example.sponsors.repository.LocationRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class LocationService {
    private final LocationRepository locationRepository;

    public LocationService(LocationRepository locationRepository) {
        this.locationRepository = locationRepository;
    }

    public Location saveLocation(Location location) {
        return locationRepository.save(location);
    }

    public Optional<Location> getLocationById(Long id) {
        return locationRepository.findById(id);
    }
}
