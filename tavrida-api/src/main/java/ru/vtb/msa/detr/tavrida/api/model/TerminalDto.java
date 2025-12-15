package ru.vtb.msa.detr.tavrida.api.model;

import java.util.UUID;

public class TerminalDto {
    private Long terminalId;
    private TransportDto transport;
    private UUID terminalGuid;
    private String terminalNumber;
    private CarrierDto carrier;

    public TerminalDto() {}

    public TerminalDto(Long terminalId, TransportDto transport, UUID terminalGuid, String terminalNumber) {
        this.terminalId = terminalId;
        this.transport = transport;
        this.terminalGuid = terminalGuid;
        this.terminalNumber = terminalNumber;
    }
    public TerminalDto(Long terminalId, TransportDto transport, UUID terminalGuid, String terminalNumber, CarrierDto carrier) {
        this.terminalId = terminalId;
        this.transport = transport;
        this.terminalGuid = terminalGuid;
        this.terminalNumber = terminalNumber;
        this.carrier = carrier;
    }

    public Long getTerminalId() { return terminalId; }
    public void setTerminalId(Long terminalId) { this.terminalId = terminalId; }

    public TransportDto getTransport() { return transport; }
    public void setTransport(TransportDto transport) { this.transport = transport; }

    public UUID getTerminalGuid() { return terminalGuid; }
    public void setTerminalGuid(UUID terminalGuid) { this.terminalGuid = terminalGuid; }

    public String getTerminalNumber() { return terminalNumber; }
    public void setTerminalNumber(String terminalNumber) { this.terminalNumber = terminalNumber; }

    public CarrierDto getCarrier() {
        return carrier;
    }

    public void setCarrier(CarrierDto carrier) {
        this.carrier = carrier;
    }
}