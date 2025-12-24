package ru.vtb.msa.detr.tavrida.api.model.terminal;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.UUID;

public class DriverTripStartRequest {

    @Schema(
        description = "UUID сессии",
        example = "123e4567-e89b-42d3-a456-556642440000",
        requiredMode = Schema.RequiredMode.REQUIRED
    )
    private UUID sessionId;
    @Schema(
        description = "UUID маршрута/пути",
        example = "123e4567-e89b-42d3-a456-556642440000",
        requiredMode = Schema.RequiredMode.REQUIRED
    )
    private UUID routeGuid;

    @Schema(
        description = "Временная метка с терминала",
        example = "2025-12-23T21:52:08.754+0300",
        requiredMode = Schema.RequiredMode.REQUIRED
    )
    private String terminalLocalStartTime;

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

    public String getTerminalLocalStartTime() {
        return terminalLocalStartTime;
    }

    public void setTerminalLocalStartTime(String terminalLocalStartTime) {
        this.terminalLocalStartTime = terminalLocalStartTime;
    }
}
