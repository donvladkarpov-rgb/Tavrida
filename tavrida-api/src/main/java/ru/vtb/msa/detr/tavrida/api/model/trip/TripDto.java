package ru.vtb.msa.detr.tavrida.api.model.trip;

import java.io.Serializable;
import java.time.Instant;
import java.util.UUID;

public class TripDto implements Serializable {
    private Long tripId;
    private Long routeId;
    private UUID sessionId;
    private Instant startedAtServer;
    private Instant closedAtServer;
    private Instant startedAtTerminal;
    private Instant closedAtTerminal;

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

    public Instant getStartedAtServer() {
        return startedAtServer;
    }

    public void setStartedAtServer(Instant startedAtServer) {
        this.startedAtServer = startedAtServer;
    }

    public Instant getClosedAtServer() {
        return closedAtServer;
    }

    public void setClosedAtServer(Instant closedAtServer) {
        this.closedAtServer = closedAtServer;
    }

    public Instant getStartedAtTerminal() {
        return startedAtTerminal;
    }

    public void setStartedAtTerminal(Instant startedAtTerminal) {
        this.startedAtTerminal = startedAtTerminal;
    }

    public Instant getClosedAtTerminal() {
        return closedAtTerminal;
    }

    public void setClosedAtTerminal(Instant closedAtTerminal) {
        this.closedAtTerminal = closedAtTerminal;
    }
}