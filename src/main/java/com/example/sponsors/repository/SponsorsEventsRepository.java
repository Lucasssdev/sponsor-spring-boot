package com.example.sponsors.repository;

import com.example.sponsors.model.SponsorsEvents;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface SponsorsEventsRepository extends JpaRepository<SponsorsEvents, Long>{
    List<SponsorsEvents> findBySponsorId(Long sponsorId);
    List<SponsorsEvents> findByEventId(Long eventId);
}
