package com.example.sponsors.service;

import com.example.sponsors.model.Events;
import com.example.sponsors.model.Sponsors;
import com.example.sponsors.model.User;
import com.example.sponsors.repository.SponsorsRepository;
import com.example.sponsors.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.time.LocalDateTime;

@Service
public class SponsorsService {

    @Autowired
    private SponsorsRepository sponsorsRepository;

    @Autowired
    private UserRepository userRepository; // Assuming you have a UserRepository

    public List<Sponsors> findAll() {
        return sponsorsRepository.findAll();
    }

    public Optional<Sponsors> findById(Long id) {
        return sponsorsRepository.findById(id);
    }

    public Sponsors save(Map<String, Object> payload) {
        String name = (String) payload.get("name");
        String cnpj = (String) payload.get("cnpj");
        String description = (String) payload.get("description");
        Long createdById = ((Number) payload.get("created_by")).longValue();
        Sponsors sponsor = new Sponsors();
        sponsor.setName(name);
        sponsor.setCnpj(cnpj);
        sponsor.setDescription(description);

        User createdBy = userRepository.findById(createdById)
                .orElseThrow(() -> new RuntimeException("User not found with ID " + createdById));
        sponsor.setCreatedBy(createdBy);
        sponsor.setCreatedAt(LocalDateTime.now());


        return sponsorsRepository.save(sponsor);
    }

    public Sponsors update(Long id, Sponsors sponsor) {
        Sponsors existingSponsor = sponsorsRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Sponsor not found with ID " + id));

        // Atualize apenas os campos não nulos do objeto recebido
        if (sponsor.getName() != null) {
            existingSponsor.setName(sponsor.getName());
        }
        if (sponsor.getCnpj() != null) {
            existingSponsor.setCnpj(sponsor.getCnpj());
        }
        if (sponsor.getDescription() != null) {
            existingSponsor.setDescription(sponsor.getDescription());
        }

        // Atualize os timestamps (se necessário)
        existingSponsor.setUpdatedAt(LocalDateTime.now());

        return sponsorsRepository.save(existingSponsor);
    }


    public void deleteById(Long id) {
        sponsorsRepository.deleteById(id);
    }

}
