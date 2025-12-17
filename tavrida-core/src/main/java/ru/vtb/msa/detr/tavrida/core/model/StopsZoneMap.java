package ru.vtb.msa.detr.tavrida.core.model;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.Instant;

@Entity
@Table(name = "STOPS_ZONE_MAP")
public class StopsZoneMap {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "STOPS_ZONE_MAP_ID")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "STOP_ID", nullable = false)
    private TransportStop stop;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ZONE_ID", nullable = false)
    private FareZone zone;

    @CreationTimestamp
    @Column(name = "CREATED_AT", nullable = false, updatable = false)
    private Instant createdAt;

    @UpdateTimestamp
    @Column(name = "UPDATED_AT", nullable = false)
    private Instant updatedAt;

    // Конструкторы
    public StopsZoneMap() {}

    public StopsZoneMap(TransportStop stop, FareZone zone) {
        this.stop = stop;
        this.zone = zone;
    }

    // Геттеры и сеттеры
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public TransportStop getStop() {
        return stop;
    }

    public void setStop(TransportStop stop) {
        this.stop = stop;
    }

    public FareZone getZone() {
        return zone;
    }

    public void setZone(FareZone zone) {
        this.zone = zone;
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