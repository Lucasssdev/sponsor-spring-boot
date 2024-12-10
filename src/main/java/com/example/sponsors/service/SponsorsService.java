package com.example.sponsors.service;

import com.example.sponsors.model.Sponsors;
import com.example.sponsors.repository.SponsorsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class SponsorsService {

    @Autowired
    private SponsorsRepository sponsorsRepository;

    public List<Sponsors> findAll() {
        return sponsorsRepository.findAll();
    }

    public Optional<Sponsors> findById(Long id) {
        return sponsorsRepository.findById(id);
    }

    public Sponsors save(Sponsors sponsor) {
        return sponsorsRepository.save(sponsor);
    }

    public Sponsors update(Long id, Sponsors sponsor) {
        Sponsors existingSponsor = sponsorsRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Sponsor not found"));
        existingSponsor.setName(sponsor.getName());
        existingSponsor.setCnpj(sponsor.getCnpj());
        existingSponsor.setDescription(sponsor.getDescription());
        return sponsorsRepository.save(existingSponsor);
    }

    public void deleteById(Long id) {
        sponsorsRepository.deleteById(id);
    }
}
