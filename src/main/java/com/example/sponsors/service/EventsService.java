package com.example.sponsors.service;

import com.example.sponsors.model.Events;
import com.example.sponsors.repository.EventsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class EventsService {

    @Autowired
    private EventsRepository eventsRepository;

    public List<Events> findAll() {
        return eventsRepository.findAll();
    }

    public Optional<Events> findById(Long id) {
        return eventsRepository.findById(id);
    }

    public Events save(Events events) {
        return eventsRepository.save(events);
    }

    public Events update(Long id, Events events) {
        Events existingEvents = eventsRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Events not found"));
        if (events.getName() != null) {
            existingEvents.setName(events.getName());
        }
        if (events.getDescription()!=null){
            existingEvents.setDescription(events.getDescription());
        }
        return eventsRepository.save(existingEvents);
    }

    public void deleteById(Long id) {
        eventsRepository.deleteById(id);
    }
}