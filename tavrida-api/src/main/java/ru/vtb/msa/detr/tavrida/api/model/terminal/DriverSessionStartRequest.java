package ru.vtb.msa.detr.tavrida.api.model.terminal;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.UUID;

public class DriverSessionStartRequest {
    @Schema(
        description = "UUID терминала",
        example = "123e4567-e89b-42d3-a456-556642440000",
        requiredMode = Schema.RequiredMode.REQUIRED
    )
    private UUID terminalGuid;
    @Schema(
        description = "UUID транспортного средства",
        example = "123e4567-e89b-42d3-a456-556642440000",
        requiredMode = Schema.RequiredMode.REQUIRED
    )
    private UUID transportGuid;
    @Schema(
        description = "UUID карты водителя",
        example = "123e4567-e89b-42d3-a456-556642440000",
        requiredMode = Schema.RequiredMode.REQUIRED
    )
    private UUID driverCardGuid;
    @Schema(
        description = "Временная метка с терминала",
        example = "2025-12-23T21:52:08.754+0300",
        requiredMode = Schema.RequiredMode.REQUIRED
    )
    private String sessionStartTime;

    // Getters & Setters
    public UUID getTerminalGuid() {
        return terminalGuid;
    }

    public void setTerminalGuid(UUID terminalGuid) {
        this.terminalGuid = terminalGuid;
    }

    public UUID getTransportGuid() {
        return transportGuid;
    }

    public void setTransportGuid(UUID transportGuid) {
        this.transportGuid = transportGuid;
    }

    public UUID getDriverCardGuid() {
        return driverCardGuid;
    }

    public void setDriverCardGuid(UUID driverCardGuid) {
        this.driverCardGuid = driverCardGuid;
    }

    public String getSessionStartTime() {
        return sessionStartTime;
    }

    public void setSessionStartTime(String sessionStartTime) {
        this.sessionStartTime = sessionStartTime;
    }
}