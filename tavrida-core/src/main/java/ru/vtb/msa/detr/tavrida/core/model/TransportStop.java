package ru.vtb.msa.detr.tavrida.core.model;

import jakarta.persistence.*;

import java.time.Instant;

@Entity
@Table(name = "transport_stops")
public class TransportStop {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "stop_id")
    private Long stopId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fare_zone_id", nullable = false)
    private FareZone fareZone;

    @Column(name = "stop_code", nullable = false, unique = true, length = 20)
    private String stopCode;

    @Column(name = "stop_name", nullable = false, length = 200)
    private String stopName;

    @Column(name = "stop_address", length = 500)
    private String stopAddress;

    // Для GEOGRAPHY используем String или специальный тип (например, через Hibernate Spatial)
    @Column(name = "geo_location", columnDefinition = "GEOGRAPHY(POINT,4326)")
    private String geoLocation;

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    @Column(name = "is_active", nullable = false)
    private Boolean isActive = true;

    @Column(name = "created_at", nullable = false)
    private Instant createdAt;

    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt;

    public Long getStopId() {
        return stopId;
    }

    public void setStopId(Long stopId) {
        this.stopId = stopId;
    }

    public FareZone getFareZone() {
        return fareZone;
    }

    public void setFareZone(FareZone fareZone) {
        this.fareZone = fareZone;
    }

    public String getStopCode() {
        return stopCode;
    }

    public void setStopCode(String stopCode) {
        this.stopCode = stopCode;
    }

    public String getStopName() {
        return stopName;
    }

    public void setStopName(String stopName) {
        this.stopName = stopName;
    }

    public String getStopAddress() {
        return stopAddress;
    }

    public void setStopAddress(String stopAddress) {
        this.stopAddress = stopAddress;
    }

    public String getGeoLocation() {
        return geoLocation;
    }

    public void setGeoLocation(String geoLocation) {
        this.geoLocation = geoLocation;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Boolean getActive() {
        return isActive;
    }

    public void setActive(Boolean active) {
        isActive = active;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Instant updatedAt) {
        this.updatedAt = updatedAt;
    }
}