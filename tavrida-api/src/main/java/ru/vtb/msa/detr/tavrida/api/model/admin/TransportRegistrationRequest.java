package ru.vtb.msa.detr.tavrida.api.model.admin;

import java.util.UUID;

public class TransportRegistrationRequest {
    private UUID transportGuid;
    private Long carrierId;
    private String transportNumber;
    private String transportName;

    // getters / setters
    public UUID getTransportGuid() { return transportGuid; }
    public void setTransportGuid(UUID transportGuid) { this.transportGuid = transportGuid; }

    public Long getCarrierId() { return carrierId; }
    public void setCarrierId(Long carrierId) { this.carrierId = carrierId; }

    public String getTransportNumber() { return transportNumber; }
    public void setTransportNumber(String transportNumber) { this.transportNumber = transportNumber; }

    public String getTransportName() { return transportName; }
    public void setTransportName(String transportName) { this.transportName = transportName; }
}