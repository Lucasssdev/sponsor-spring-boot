package com.example.sponsors.repository;

import com.example.sponsors.model.Sponsors;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SponsorsRepository extends JpaRepository<Sponsors, Long> {
}
