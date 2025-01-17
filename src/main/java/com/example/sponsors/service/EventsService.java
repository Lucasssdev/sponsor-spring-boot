package com.example.sponsors.service;

import com.example.sponsors.model.Events;
import com.example.sponsors.model.Location;
import com.example.sponsors.model.Sponsors;
import com.example.sponsors.model.User;
import com.example.sponsors.repository.EventsRepository;
import com.example.sponsors.repository.SponsorsRepository;
import com.example.sponsors.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class EventsService {

    @Autowired
    private EventsRepository eventsRepository;

    @Autowired
    private SponsorsRepository sponsorsRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private LocationService locationService;

    @Autowired
    private GeocodingService geocodingService; // Para buscar coordenadas

    public List<Events> findAll() {
        return eventsRepository.findAll();
    }

    public Optional<Events> findById(Long id) {
        return eventsRepository.findById(id);
    }

    public Events save(Map<String, Object> payload) {
        // Criar um novo evento
        Events event = new Events();

        event.setName((String) payload.get("name"));
        event.setDescription((String) payload.get("description"));
        event.setStartDate(LocalDate.parse((String) payload.get("startDate")));
        event.setStartTime(LocalTime.parse((String) payload.get("startTime")));

        // 📌 1️⃣ Verificar se a localização já existe pelo ID
        Long locationId = payload.get("locationId") != null ? ((Number) payload.get("locationId")).longValue() : null;
        Location location = null;

        if (locationId != null) {
            location = locationService.getLocationById(locationId)
                    .orElseThrow(() -> new RuntimeException("Localização não encontrada!"));
        } else {
            // 📌 2️⃣ Criar nova localização usando o endereço fornecido
            String address = (String) payload.get("address");
            if (address == null || address.isEmpty()) {
                throw new RuntimeException("Endereço é obrigatório para criar a localização!");
            }

            location = geocodingService.getCoordinates(address);
            if (location == null) {
                throw new RuntimeException("Falha ao obter coordenadas para o endereço!");
            }

            location = locationService.saveLocation(location);
        }

        // Definir localização no evento
        event.setLocation(location);

        // 📌 3️⃣ Associar Criador do Evento
        Long createdById = ((Number) payload.get("created_by")).longValue();
        User createdBy = userRepository.findById(createdById)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado com ID " + createdById));
        event.setCreatedBy(createdBy);

        // 📌 4️⃣ Associar Patrocinadores
        List<Integer> sponsorIdsList = (List<Integer>) payload.get("sponsors");
        List<Long> sponsorIds = sponsorIdsList.stream()
                .map(Integer::longValue)
                .toList();

        Set<Sponsors> sponsors = sponsorIds.stream()
                .map(id -> sponsorsRepository.findById(id)
                        .orElseThrow(() -> new RuntimeException("Patrocinador não encontrado com ID " + id)))
                .collect(Collectors.toSet());
        event.setSponsors(sponsors);

        event.setCreatedAt(LocalDateTime.now());

        return eventsRepository.save(event);
    }

    public Events update(Long id, Events events) {
        Events existingEvents = eventsRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Evento não encontrado"));

        if (events.getName() != null) {
            existingEvents.setName(events.getName());
        }
        if (events.getDescription() != null) {
            existingEvents.setDescription(events.getDescription());
        }
        existingEvents.setUpdatedAt(LocalDateTime.now());

        return eventsRepository.save(existingEvents);
    }

    public void deleteById(Long id) {
        eventsRepository.deleteById(id);
    }

    public Events associateLocation(Long eventId, Long locationId) {
        Events event = eventsRepository.findById(eventId)
                .orElseThrow(() -> new RuntimeException("Evento não encontrado!"));

        Location location = locationService.getLocationById(locationId)
                .orElseThrow(() -> new RuntimeException("Localização não encontrada!"));

        event.setLocation(location);
        return eventsRepository.save(event);
    }
}
