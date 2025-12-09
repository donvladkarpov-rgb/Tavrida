package ru.vtb.msa.detr.tavrida.api.model.terminal;

import java.util.UUID;

public class TerminalActivationRequest {
    private UUID terminalGuid;
    private UUID transportGuid;
    private UUID cardGuid; // Новая карта активации
    private String terminalNumber;
    private String terminalSerialNumber;

    // getters/setters
    public UUID getTerminalGuid() { return terminalGuid; }
    public void setTerminalGuid(UUID terminalGuid) { this.terminalGuid = terminalGuid; }

    public UUID getCardGuid() { return cardGuid; }
    public void setCardGuid(UUID cardGuid) { this.cardGuid = cardGuid; }

    public UUID getTransportGuid() {
        return transportGuid;
    }

    public void setTransportGuid(UUID transportGuid) {
        this.transportGuid = transportGuid;
    }

    public String getTerminalNumber() {
        return terminalNumber;
    }

    public void setTerminalNumber(String terminalNumber) {
        this.terminalNumber = terminalNumber;
    }

    public String getTerminalSerialNumber() {
        return terminalSerialNumber;
    }

    public void setTerminalSerialNumber(String terminalSerialNumber) {
        this.terminalSerialNumber = terminalSerialNumber;
    }
}