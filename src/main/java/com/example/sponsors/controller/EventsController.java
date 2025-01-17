package com.example.sponsors.controller;

import com.example.sponsors.model.Events;
import com.example.sponsors.model.Location;
import com.example.sponsors.repository.EventsRepository;
import com.example.sponsors.service.EventsService;
import com.example.sponsors.service.GeocodingService;
import com.example.sponsors.service.LocationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
@RestController
@RequestMapping("/api/events")
public class EventsController {

    @Autowired
    private EventsService eventsService;

    @Autowired
    private GeocodingService geocodingService;
    @Autowired
    private LocationService locationService;
    @Autowired
    private EventsRepository eventsRepository;

    @GetMapping
    public ResponseEntity<List<Events>> getAllEvents() {
        return ResponseEntity.ok(eventsService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Events> getEventById(@PathVariable Long id) {
        return eventsService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/register")
    public ResponseEntity<Events> createEvent(@RequestBody Map<String, Object> payload) {
        Events savedEvent = eventsService.save(payload);
        return ResponseEntity.ok(savedEvent);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Events> updateEvent(@PathVariable Long id, @RequestBody Events events) {
        return ResponseEntity.ok(eventsService.update(id, events));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEvent(@PathVariable Long id) {
        eventsService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/geocode")
    public Location getLocationFromAddress(@RequestParam String address) {
        Location location = geocodingService.getCoordinates(address);
        if (location == null) {
            throw new RuntimeException("Endereço inválido");
        }
        return locationService.saveLocation(location);
    }

    @PostMapping("/{eventId}/associate-location")
    public Events associateLocationToEvent(@PathVariable Long eventId, @RequestParam Long locationId) {
        Events events = eventsRepository.findById(eventId)
                .orElseThrow(() -> new RuntimeException("Evento não encontrado"));
        Location location = locationService.getLocationById(locationId)
                .orElseThrow(() -> new RuntimeException("Localização não encontrada"));

        events.setLocation(location);
        return eventsRepository.save(events);
    }


}
