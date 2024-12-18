package com.example.sponsors.controller;

import com.example.sponsors.model.Events;
import com.example.sponsors.service.EventsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/events")
public class EventsController {

    @Autowired
    private EventsService eventsService;

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

    @PostMapping
    public ResponseEntity<Events> createEvent(@RequestBody Events events) {
        return ResponseEntity.ok(eventsService.save(events));
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
}