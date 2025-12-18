package ru.vtb.msa.detr.tavrida.api.model.cashier;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;
import java.util.UUID;

public class CardInitRequest {
    @Schema(
            description = "UUID идентификатор сессии",
            example = "123e4567-e89b-42d3-a456-756642440001",
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    private UUID sessionId;
    @Schema(
            description = "Местное время",
            example = "2007-12-03T10:15:3"
    )
    private LocalDateTime localStartTime;
    @Schema(
            description = "Часовой пояс местного времени",
            example = "Z - UTC, +h, +hh, +hh:mm, -h, -hh, -hh:mm",
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    private String timeZoneOffset;

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

    public LocalDateTime getLocalStartTime() {
        return localStartTime;
    }

    public void setLocalStartTime(LocalDateTime localStartTime) {
        this.localStartTime = localStartTime;
    }

    public String getTimeZoneOffset() {
        return timeZoneOffset;
    }

    public void setTimeZoneOffset(String timeZoneOffset) {
        this.timeZoneOffset = timeZoneOffset;
    }
}
