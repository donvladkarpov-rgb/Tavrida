package ru.vtb.msa.detr.tavrida.api.model;

import java.util.UUID;

public class TransportDto {
    private Long transportId;
    private UUID transportGuid;
    private CarrierDto carrier;
    private String transportNumber;
    private String transportName;

    public TransportDto() {}

    public TransportDto(Long transportId, UUID transportGuid, CarrierDto carrier, String transportNumber, String transportName) {
        this.transportId = transportId;
        this.transportGuid = transportGuid;
        this.carrier = carrier;
        this.transportNumber = transportNumber;
        this.transportName = transportName;
    }

    public Long getTransportId() { return transportId; }
    public void setTransportId(Long transportId) { this.transportId = transportId; }

    public UUID getTransportGuid() { return transportGuid; }
    public void setTransportGuid(UUID transportGuid) { this.transportGuid = transportGuid; }

    public CarrierDto getCarrier() { return carrier; }
    public void setCarrier(CarrierDto carrier) { this.carrier = carrier; }

    public String getTransportNumber() { return transportNumber; }
    public void setTransportNumber(String transportNumber) { this.transportNumber = transportNumber; }

    public String getTransportName() { return transportName; }
    public void setTransportName(String transportName) { this.transportName = transportName; }
}