package ru.vtb.msa.detr.tavrida.api.model.carrier;

import java.io.Serializable;
import java.time.Instant;

public class CarrierRouteMapDto implements Serializable {
    private Long carrierRouteMapId;
    private Long carrierId;
    private Long routeId;
    private Instant createdAt;
    private Instant updatedAt;

    public CarrierRouteMapDto() {}

    public Long getCarrierRouteMapId() {
        return carrierRouteMapId;
    }

    public void setCarrierRouteMapId(Long carrierRouteMapId) {
        this.carrierRouteMapId = carrierRouteMapId;
    }

    public Long getCarrierId() {
        return carrierId;
    }

    public void setCarrierId(Long carrierId) {
        this.carrierId = carrierId;
    }

    public Long getRouteId() {
        return routeId;
    }

    public void setRouteId(Long routeId) {
        this.routeId = routeId;
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