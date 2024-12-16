package com.example.sponsors.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;
@Entity
@Table(name = "sponsors_events")
public class SponsorsEvents {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "sponsor_id", nullable = false)
    private Sponsors sponsor;

    @ManyToOne
    @JoinColumn(name = "event_id", nullable = false)
    private Events event;

    @Column(name = "sponsorship_amount")
    private Double sponsorshipAmount;

    @Column(name = "association_date")
    private LocalDateTime associationDate;

    // Construtores
    public SponsorsEvents() {
    }

    public SponsorsEvents(Sponsors sponsor, Events event, Double sponsorshipAmount) {
        this.sponsor = sponsor;
        this.event = event;
        this.sponsorshipAmount = sponsorshipAmount;
        this.associationDate = LocalDateTime.now();
    }

    // Getters e Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Sponsors getSponsor() {
        return sponsor;
    }

    public void setSponsor(Sponsors sponsor) {
        this.sponsor = sponsor;
    }

    public Events getEvent() {
        return event;
    }

    public void setEvent(Events event) {
        this.event = event;
    }

    public Double getSponsorshipAmount() {
        return sponsorshipAmount;
    }

    public void setSponsorshipAmount(Double sponsorshipAmount) {
        this.sponsorshipAmount = sponsorshipAmount;
    }

    public LocalDateTime getAssociationDate() {
        return associationDate;
    }

    public void setAssociationDate(LocalDateTime associationDate) {
        this.associationDate = associationDate;
    }



}

