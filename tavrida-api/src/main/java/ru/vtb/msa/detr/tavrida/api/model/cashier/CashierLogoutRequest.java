package ru.vtb.msa.detr.tavrida.api.model.cashier;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;
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
            description = "Местное время",
            example = "2007-12-03T10:15:3",
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    private LocalDateTime logoutStartTime;
    @Schema(
            description = "Часовой пояс местного времени",
            example = "Z - UTC, +h, +hh, +hh:mm, -h, -hh, -hh:mm",
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    private String timeZoneOffset;

    public UUID getSessionId() {
        return sessionId;
    }

    public void setSessionId(UUID sessionId) {
        this.sessionId = sessionId;
    }

    public LocalDateTime getLogoutStartTime() {
        return logoutStartTime;
    }

    public void setLogoutStartTime(LocalDateTime logoutStartTime) {
        this.logoutStartTime = logoutStartTime;
    }

    public String getTimeZoneOffset() {
        return timeZoneOffset;
    }

    public void setTimeZoneOffset(String timeZoneOffset) {
        this.timeZoneOffset = timeZoneOffset;
    }
}
