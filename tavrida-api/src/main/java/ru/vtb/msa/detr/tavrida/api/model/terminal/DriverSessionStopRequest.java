package ru.vtb.msa.detr.tavrida.api.model.terminal;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.UUID;

public class DriverSessionStopRequest {
    @JsonProperty("sessionId")
    @Schema(
        description = "Идентификатор сессии",
        example = "123e4567-e89b-42d3-a456-556642440000",
        requiredMode = Schema.RequiredMode.REQUIRED
    )
    private UUID sessionId;

    @Schema(
        description = "Временная метка с терминала",
        example = "2025-12-23T21:52:08.754+0300",
        requiredMode = Schema.RequiredMode.REQUIRED
    )
    private String sessionStartTime;

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

    public String getSessionStartTime() {
        return sessionStartTime;
    }

    public void setSessionStartTime(String sessionStartTime) {
        this.sessionStartTime = sessionStartTime;
    }
}
