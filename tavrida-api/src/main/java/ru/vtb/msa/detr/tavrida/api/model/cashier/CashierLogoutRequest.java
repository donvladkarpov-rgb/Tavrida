package ru.vtb.msa.detr.tavrida.api.model.cashier;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.UUID;

public class CashierLogoutRequest {
    @JsonProperty("sessionId")
    private UUID sessionId;

    public CashierLogoutRequest() {
    }

    public CashierLogoutRequest(UUID sessionId) {
        this.sessionId = sessionId;
    }

    public UUID getSessionId() {
        return sessionId;
    }

    public void setSessionId(UUID sessionId) {
        this.sessionId = sessionId;
    }

}
