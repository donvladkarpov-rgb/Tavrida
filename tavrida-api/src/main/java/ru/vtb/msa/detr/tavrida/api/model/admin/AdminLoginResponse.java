package ru.vtb.msa.detr.tavrida.api.model.admin;

import java.util.UUID;

public class AdminLoginResponse {
    private UUID sessionId;

    public AdminLoginResponse() {
    }

    public AdminLoginResponse(UUID sessionId) {
        this.sessionId = sessionId;
    }

    public UUID getSessionId() {
        return sessionId;
    }

    public void setSessionId(UUID sessionId) {
        this.sessionId = sessionId;
    }
}
