package ru.vtb.msa.detr.tavrida.core.model;

import jakarta.persistence.*;

import java.time.Instant;

@Entity
@Table(name = "stops_route_map")
public class StopsRouteMap {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "stops_route_map_id")
    private Long stopsRouteMapId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "stop_id", nullable = false)
    private TransportStop stop;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "route_id", nullable = false)
    private Route route;

    @Column(name = "serial_number", nullable = false)
    private Integer serialNumber;

    @Column(name = "created_at", nullable = false)
    private Instant createdAt;

    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt;

    public Long getStopsRouteMapId() {
        return stopsRouteMapId;
    }

    public void setStopsRouteMapId(Long stopsRouteMapId) {
        this.stopsRouteMapId = stopsRouteMapId;
    }

    public TransportStop getStop() {
        return stop;
    }

    public void setStop(TransportStop stop) {
        this.stop = stop;
    }

    public Route getRoute() {
        return route;
    }

    public void setRoute(Route route) {
        this.route = route;
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