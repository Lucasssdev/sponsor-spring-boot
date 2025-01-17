package com.example.sponsors.controller;

import com.example.sponsors.model.Sponsors;
import com.example.sponsors.service.SponsorsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.List;

@RestController
@RequestMapping("/api/sponsors")
public class SponsorsController {

    @Autowired
    private SponsorsService sponsorsService;

    @GetMapping
    public ResponseEntity<List<Sponsors>> getAllSponsors() {
        return ResponseEntity.ok(sponsorsService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Sponsors> getSponsorById(@PathVariable Long id) {
        return sponsorsService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/register")
    public ResponseEntity<Sponsors> createSponsor(@RequestBody Map<String, Object> payload) {
        Sponsors savedSponsor = sponsorsService.save(payload);
        return ResponseEntity.ok(savedSponsor);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Sponsors> updateSponsor(@PathVariable Long id, @RequestBody Sponsors sponsor) {
        return ResponseEntity.ok(sponsorsService.update(id, sponsor));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSponsor(@PathVariable Long id) {
        sponsorsService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
