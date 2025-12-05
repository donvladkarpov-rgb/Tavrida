package ru.vtb.msa.detr.tavrida.api.model.admin;

import java.util.UUID;

public class CarrierRegistrationRequest {
    private UUID sessionId;

    private String carrierName;

    public CarrierRegistrationRequest() {}

    public CarrierRegistrationRequest(
            String carrierName,
            UUID sessionId
    ) {
        this.carrierName = carrierName;
        this.sessionId = sessionId;
    }

    public String getCarrierName() { return carrierName; }
    public void setCarrierName(String carrierName) { this.carrierName = carrierName; }

    public UUID getSessionId() {
        return sessionId;
    }

    public void setSessionId(UUID sessionId) {
        this.sessionId = sessionId;
    }
}