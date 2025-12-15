package ru.vtb.msa.detr.tavrida.core.model;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.Instant;

@Entity
@Table(name = "carrier_zone_map")
public class CarrierZoneMap {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "carrier_zone_map_id")
    private Long carrierZoneMapId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "carrier_id", nullable = false)
    private Carrier carrier;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "zone_id", nullable = false)
    private FareZone fareZone;

    @Column(name = "base_fare", precision = 10, scale = 2, nullable = false)
    private BigDecimal baseFare;

    @Column(name = "created_at", nullable = false)
    private Instant createdAt;

    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt;

    public Long getCarrierZoneMapId() {
        return carrierZoneMapId;
    }

    public void setCarrierZoneMapId(Long carrierZoneMapId) {
        this.carrierZoneMapId = carrierZoneMapId;
    }

    public Carrier getCarrier() {
        return carrier;
    }

    public void setCarrier(Carrier carrier) {
        this.carrier = carrier;
    }

    public FareZone getFareZone() {
        return fareZone;
    }

    public void setFareZone(FareZone fareZone) {
        this.fareZone = fareZone;
    }

    public BigDecimal getBaseFare() {
        return baseFare;
    }

    public void setBaseFare(BigDecimal baseFare) {
        this.baseFare = baseFare;
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