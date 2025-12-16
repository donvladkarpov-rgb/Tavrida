package ru.vtb.msa.detr.tavrida.api.model.terminal;

import java.util.UUID;

public class DriverTripStartRequest {

    private UUID sessionId;
    private UUID routeGuid;

    public UUID getSessionId() {
        return sessionId;
    }

    public void setSessionId(UUID sessionId) {
        this.sessionId = sessionId;
    }

    public UUID getRouteGuid() {
        return routeGuid;
    }

    public void setRouteGuid(UUID routeGuid) {
        this.routeGuid = routeGuid;
    }

}
