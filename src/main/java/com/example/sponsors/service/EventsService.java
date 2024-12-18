package com.example.sponsors.service;

import com.example.sponsors.model.Events;
import com.example.sponsors.model.Sponsors;
import com.example.sponsors.model.User;
import com.example.sponsors.repository.EventsRepository;
import com.example.sponsors.repository.SponsorsRepository;
import com.example.sponsors.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map; // Necessário para usar Map<String, Object>
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors; // Necessário para usar .stream() e Collectors.toSet
import java.time.LocalDate;
import java.time.LocalTime;

@Service
public class EventsService {

    @Autowired
    private EventsRepository eventsRepository;

    @Autowired
    private SponsorsRepository sponsorsRepository;

    @Autowired
    private UserRepository userRepository;

    public List<Events> findAll() {
        return eventsRepository.findAll();
    }

    public Optional<Events> findById(Long id) {
        return eventsRepository.findById(id);
    }


    public Events save(Map<String, Object> payload) {
        // Extrai os campos do payload
        String name = (String) payload.get("name");
        String description = (String) payload.get("description");
        Long createdById = ((Number) payload.get("created_by")).longValue();
        List<Integer> sponsorIdsList = (List<Integer>) payload.get("sponsors");

        // Extrai e converte startDate e startTime
        String startDateString = (String) payload.get("startDate");
        String startTimeString = (String) payload.get("startTime");

        LocalDate startDate = startDateString != null ? LocalDate.parse(startDateString) : null;
        LocalTime startTime = startTimeString != null ? LocalTime.parse(startTimeString) : null;

        // Converte a lista de Integer para List<Long>
        List<Long> sponsorIds = sponsorIdsList.stream()
                .map(Integer::longValue)
                .toList();

        // Cria uma instância de Events
        Events events = new Events();
        events.setName(name);
        events.setDescription(description);
        events.setStartDate(startDate);
        events.setStartTime(startTime);

        // Busca os patrocinadores pelo ID
        Set<Sponsors> sponsors = sponsorIds.stream()
                .map(id -> sponsorsRepository.findById(id)
                        .orElseThrow(() -> new RuntimeException("Sponsor not found with ID " + id)))
                .collect(Collectors.toSet());

        // Busca o usuário que criou o evento
        User createdBy = userRepository.findById(createdById)
                .orElseThrow(() -> new RuntimeException("User not found with ID " + createdById));

        // Adiciona os patrocinadores e o usuário criador ao evento
        events.setSponsors(sponsors);
        events.setCreatedBy(createdBy);

        // Salva o evento
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
