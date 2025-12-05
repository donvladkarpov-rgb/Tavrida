package ru.vtb.msa.detr.tavrida.api.model.terminal;

import java.util.UUID;

public class DriverSessionStartRequest {
    private UUID terminalGuid;
    private UUID transportGuid;
    private UUID driverCardGuid;

    // Getters & Setters
    public UUID getTerminalGuid() { return terminalGuid; }
    public void setTerminalGuid(UUID terminalGuid) { this.terminalGuid = terminalGuid; }

    public UUID getTransportGuid() { return transportGuid; }
    public void setTransportGuid(UUID transportGuid) { this.transportGuid = transportGuid; }

    public UUID getDriverCardGuid() { return driverCardGuid; }
    public void setDriverCardGuid(UUID driverCardGuid) { this.driverCardGuid = driverCardGuid; }
}