package ru.vtb.msa.detr.tavrida.api.model.cashier;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.UUID;

public class CardInitRequest {
    @Schema(
        description = "UUID идентификатор сессии",
        example = "123e4567-e89b-42d3-a456-756642440001",
        requiredMode = Schema.RequiredMode.REQUIRED
    )
    private UUID sessionId;

    @Schema(
        description = "Временная метка с терминала",
        example = "2025-12-23T21:52:08.754+0300",
        requiredMode = Schema.RequiredMode.REQUIRED
    )
    private String terminalStartTime;

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

    public String getTerminalStartTime() {
        return terminalStartTime;
    }

    public void setTerminalStartTime(String terminalStartTime) {
        this.terminalStartTime = terminalStartTime;
    }
}
