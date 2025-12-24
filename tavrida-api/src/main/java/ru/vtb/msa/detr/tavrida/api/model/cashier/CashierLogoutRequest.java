package ru.vtb.msa.detr.tavrida.api.model.cashier;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.UUID;

public class CashierLogoutRequest {
    @JsonProperty("sessionId")
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
    private String logoutTerminalStartTime;

    public UUID getSessionId() {
        return sessionId;
    }

    public void setSessionId(UUID sessionId) {
        this.sessionId = sessionId;
    }

    public String getLogoutTerminalStartTime() {
        return logoutTerminalStartTime;
    }

    public void setLogoutTerminalStartTime(String logoutTerminalStartTime) {
        this.logoutTerminalStartTime = logoutTerminalStartTime;
    }
}
