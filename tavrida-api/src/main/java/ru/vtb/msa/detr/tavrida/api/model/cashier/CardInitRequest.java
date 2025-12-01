package ru.vtb.msa.detr.tavrida.api.model.cashier;

import java.util.UUID;

public class CardInitRequest {
    private UUID sessionId;

    public CardInitRequest() {
    }

    public CardInitRequest(UUID sessionId) {
        this.sessionId = sessionId;
    }

    public UUID getSessionId() {
        return sessionId;
    }

    public void setSessionId(UUID sessionId) {
        this.sessionId = sessionId;
    }
}
