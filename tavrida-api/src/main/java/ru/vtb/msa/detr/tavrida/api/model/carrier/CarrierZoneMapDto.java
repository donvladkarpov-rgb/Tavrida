package ru.vtb.msa.detr.tavrida.api.model.carrier;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.Instant;

public class CarrierZoneMapDto implements Serializable {
    private Long carrierZoneMapId;
    private Long carrierId;
    private Long zoneId;
    private BigDecimal baseFare;
    private Instant createdAt;
    private Instant updatedAt;

    public CarrierZoneMapDto() {}

    public Long getCarrierZoneMapId() {
        return carrierZoneMapId;
    }

    public void setCarrierZoneMapId(Long carrierZoneMapId) {
        this.carrierZoneMapId = carrierZoneMapId;
    }

    public Long getCarrierId() {
        return carrierId;
    }

    public void setCarrierId(Long carrierId) {
        this.carrierId = carrierId;
    }

    public Long getZoneId() {
        return zoneId;
    }

    public void setZoneId(Long zoneId) {
        this.zoneId = zoneId;
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