package ru.vtb.msa.detr.tavrida.api.model.trip;

import java.io.Serializable;
import java.time.Instant;
import java.util.UUID;

public class TripDto implements Serializable {
    private Long tripId;
    private Long routeId;
    private UUID sessionId;
    private Instant startedAt;
    private Instant closedAt;
    private Instant startedAtLocal;
    private Instant closedAtLocal;

    public TripDto() {}

    public Long getTripId() {
        return tripId;
    }

    public void setTripId(Long tripId) {
        this.tripId = tripId;
    }

    public Long getRouteId() {
        return routeId;
    }

    public void setRouteId(Long routeId) {
        this.routeId = routeId;
    }

    public UUID getSessionId() {
        return sessionId;
    }

    public void setSessionId(UUID sessionId) {
        this.sessionId = sessionId;
    }

    public Instant getStartedAt() {
        return startedAt;
    }

    public void setStartedAt(Instant startedAt) {
        this.startedAt = startedAt;
    }

    public Instant getClosedAt() {
        return closedAt;
    }

    public void setClosedAt(Instant closedAt) {
        this.closedAt = closedAt;
    }

    public Instant getStartedAtLocal() {
        return startedAtLocal;
    }

    public void setStartedAtLocal(Instant startedAtLocal) {
        this.startedAtLocal = startedAtLocal;
    }

    public Instant getClosedAtLocal() {
        return closedAtLocal;
    }

    public void setClosedAtLocal(Instant closedAtLocal) {
        this.closedAtLocal = closedAtLocal;
    }
}