package ru.vtb.msa.detr.tavrida.api.model.cashier;

import java.util.UUID;

public class CashierLoginResponse {
    private UUID sessionId;

    public CashierLoginResponse() {
    }

    public CashierLoginResponse(UUID sessionId) {
        this.sessionId = sessionId;
    }

    public UUID getSessionId() {
        return sessionId;
    }

    public void setSessionId(UUID sessionId) {
        this.sessionId = sessionId;
    }
}
