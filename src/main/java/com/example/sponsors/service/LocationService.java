package com.example.sponsors.service;

import com.example.sponsors.model.Location;
import com.example.sponsors.repository.LocationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Optional;

@Service
public class LocationService {

    @Autowired
    private GeocodingService geocodingService;

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
    public Location createLocationFromPayload(Map<String, Object> payload) {
        String address = (String) payload.get("address");
        Double latitude = payload.get("latitude") != null ? ((Number) payload.get("latitude")).doubleValue() : null;
        Double longitude = payload.get("longitude") != null ? ((Number) payload.get("longitude")).doubleValue() : null;

        // 📌 Se latitude e longitude não foram informadas, buscar pelo endereço
        if (latitude == null || longitude == null) {
            if (address == null || address.isEmpty()) {
                throw new IllegalArgumentException("Endereço, latitude e longitude são obrigatórios!");
            }

            Location location = geocodingService.getCoordinates(address);
            if (location == null) {
                throw new RuntimeException("Falha ao obter coordenadas para o endereço!");
            }
            return locationRepository.save(location);
        }

        // 📌 Se latitude e longitude foram informadas, criar a localização diretamente
        Location location = new Location(address, latitude, longitude);
        return locationRepository.save(location);
    }
}
