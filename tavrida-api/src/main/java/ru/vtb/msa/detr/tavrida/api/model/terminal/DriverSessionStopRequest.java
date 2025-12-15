package ru.vtb.msa.detr.tavrida.api.model.terminal;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.UUID;

public class DriverSessionStopRequest
{
    @JsonProperty("sessionId")
    private UUID sessionId;

    public DriverSessionStopRequest() {
    }

    public DriverSessionStopRequest(UUID sessionId) {
        this.sessionId = sessionId;
    }

    public UUID getSessionId() {
        return sessionId;
    }

    public void setSessionId(UUID sessionId) {
        this.sessionId = sessionId;
    }
}
