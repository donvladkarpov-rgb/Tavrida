package ru.vtb.msa.detr.tavrida.api.model.admin;

import java.util.UUID;

public class TerminalRegistrationRequest {
    private UUID terminalGuid;
    private Long transportId;
    private String terminalNumber;

    // getters / setters
    public UUID getTerminalGuid() { return terminalGuid; }
    public void setTerminalGuid(UUID terminalGuid) { this.terminalGuid = terminalGuid; }

    public Long getTransportId() { return transportId; }
    public void setTransportId(Long transportId) { this.transportId = transportId; }

    public String getTerminalNumber() { return terminalNumber; }
    public void setTerminalNumber(String terminalNumber) { this.terminalNumber = terminalNumber; }
}