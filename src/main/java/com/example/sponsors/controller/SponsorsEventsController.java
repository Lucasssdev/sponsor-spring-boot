package com.example.sponsors.controller;

import com.example.sponsors.model.SponsorsEvents;
import com.example.sponsors.service.SponsorsEventsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/sponsors-events")
public class SponsorsEventsController {

    @Autowired
    private SponsorsEventsService sponsorsEventsService;

    @GetMapping
    public ResponseEntity<List<SponsorsEvents>> getAllSponsorsEvents() {
        return ResponseEntity.ok(sponsorsEventsService.findAllSponsorsEvents());
    }

    @GetMapping("/{id}")
    public ResponseEntity<SponsorsEvents> getAllSponsorsEvents(@PathVariable Long id) {
        return sponsorsEventsService.findSponsorsEventById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/event/{eventId}")
    public List<SponsorsEvents> getSponsorsEventsByEventId(@PathVariable Long eventId) {
        return sponsorsEventsService.findSponsorsEventsByEventId(eventId);
    }

    @GetMapping("/sponsor/{sponsorId}")
    public List<SponsorsEvents> getSponsorsEventsBySponsorId(@PathVariable Long sponsorId) {
        return sponsorsEventsService.findSponsorsEventsBySponsorId(sponsorId);
    }

    @PostMapping
    public SponsorsEvents createSponsorsEvent(@RequestBody SponsorsEvents sponsorsEvent) {
        return sponsorsEventsService.createSponsorsEvent(sponsorsEvent);
    }

    @PutMapping("/{id}")
    public ResponseEntity<SponsorsEvents> updateSponsorsEvent(
            @PathVariable Long id,
            @RequestBody SponsorsEvents sponsorsEventDetails
    ) {
        SponsorsEvents updatedSponsorsEvent = sponsorsEventsService.updateSponsorsEvent(id, sponsorsEventDetails);
        return ResponseEntity.ok(updatedSponsorsEvent);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSponsorsEvent(@PathVariable Long id) {
        sponsorsEventsService.deleteSponsorsEvent(id);
        return ResponseEntity.noContent().build();
    }

}
