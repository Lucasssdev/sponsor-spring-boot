package com.example.sponsors.service;


import com.example.sponsors.model.SponsorsEvents;
import com.example.sponsors.repository.SponsorsEventsRepository;
import com.example.sponsors.repository.SponsorsRepository;
import com.example.sponsors.repository.EventsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class SponsorsEventsService {
    @Autowired
    private SponsorsEventsRepository sponsorsEventsRepository;

    @Autowired
    private SponsorsRepository sponsorsRepository;

    @Autowired
    private EventsRepository eventsRepository;

    public List<SponsorsEvents> findAllSponsorsEvents() {
        return sponsorsEventsRepository.findAll();
    }

    public Optional<SponsorsEvents> findSponsorsEventById(Long id) {
        return sponsorsEventsRepository.findById(id);
    }

    public SponsorsEvents createSponsorsEvent(SponsorsEvents sponsorsEvent) {
        // Validar se o sponsor e o evento existem antes de criar
        sponsorsRepository.findById(sponsorsEvent.getSponsor().getId())
                .orElseThrow(() -> new RuntimeException("Sponsor not found"));

        eventsRepository.findById(sponsorsEvent.getEvent().getId())
                .orElseThrow(() -> new RuntimeException("Event not found"));

        return sponsorsEventsRepository.save(sponsorsEvent);
    }

    public SponsorsEvents updateSponsorsEvent(Long id, SponsorsEvents sponsorsEventDetails) {
        SponsorsEvents sponsorsEvent = sponsorsEventsRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Sponsors Event not found with id: " + id));

        // Validar se o novo sponsor e evento existem
        sponsorsRepository.findById(sponsorsEventDetails.getSponsor().getId())
                .orElseThrow(() -> new RuntimeException("Sponsor not found"));

        eventsRepository.findById(sponsorsEventDetails.getEvent().getId())
                .orElseThrow(() -> new RuntimeException("Event not found"));

        sponsorsEvent.setSponsor(sponsorsEventDetails.getSponsor());
        sponsorsEvent.setEvent(sponsorsEventDetails.getEvent());
        sponsorsEvent.setSponsorshipAmount(sponsorsEventDetails.getSponsorshipAmount());

        return sponsorsEventsRepository.save(sponsorsEvent);
    }

    public void deleteSponsorsEvent(Long id) {
        SponsorsEvents sponsorsEvent = sponsorsEventsRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Sponsors Event not found with id: " + id));

        sponsorsEventsRepository.delete(sponsorsEvent);
    }

    public List<SponsorsEvents> findSponsorsEventsByEventId(Long eventId) {
        // Verificar se o evento existe antes de buscar
        eventsRepository.findById(eventId)
                .orElseThrow(() -> new RuntimeException("Event not found with id: " + eventId));

        return sponsorsEventsRepository.findByEventId(eventId);
    }

    public List<SponsorsEvents> findSponsorsEventsBySponsorId(Long sponsorId) {
        // Verificar se o sponsor existe antes de buscar
        sponsorsRepository.findById(sponsorId)
                .orElseThrow(() -> new RuntimeException("Sponsor not found with id: " + sponsorId));

        return sponsorsEventsRepository.findBySponsorId(sponsorId);
    }

    // Método adicional para calcular valor total de patrocínio por evento
    public Double calculateTotalSponsorshipByEventId(Long eventId) {
        List<SponsorsEvents> sponsorsEvents = findSponsorsEventsByEventId(eventId);
        return sponsorsEvents.stream()
                .mapToDouble(SponsorsEvents::getSponsorshipAmount)
                .sum();
    }

}
