package ru.vtb.msa.detr.tavrida.api.model.stop;

import java.io.Serializable;
import java.time.Instant;

public class StopsRouteMapDto implements Serializable {
    private Long stopsRouteMapId;
    private Long stopId;
    private Long routeId;
    private Integer serialNumber;
    private Instant createdAt;
    private Instant updatedAt;

    public StopsRouteMapDto() {}

    public Long getStopsRouteMapId() {
        return stopsRouteMapId;
    }

    public void setStopsRouteMapId(Long stopsRouteMapId) {
        this.stopsRouteMapId = stopsRouteMapId;
    }

    public Long getStopId() {
        return stopId;
    }

    public void setStopId(Long stopId) {
        this.stopId = stopId;
    }

    public Long getRouteId() {
        return routeId;
    }

    public void setRouteId(Long routeId) {
        this.routeId = routeId;
    }

    public Integer getSerialNumber() {
        return serialNumber;
    }

    public void setSerialNumber(Integer serialNumber) {
        this.serialNumber = serialNumber;
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