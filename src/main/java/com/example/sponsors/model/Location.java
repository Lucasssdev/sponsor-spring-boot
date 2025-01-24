package com.example.sponsors.model;

import jakarta.persistence.*;

@Entity
@Table(name = "locations")
public class Location {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String address;
    private Double latitude;
    private Double longitude;

    public Location() {}

    public Location(String address, Double latitude, Double longitude) {
        this.address = address;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public Long getId() { return id; }
    public String getAddress() { return address; }
    public Double getLatitude() { return latitude; }
    public Double getLongitude() { return longitude; }

    public void setAddress(String address) { this.address = address; }
    public void setLatitude(Double latitude) { this.latitude = latitude; }
    public void setLongitude(Double longitude) { this.longitude = longitude; }

}
