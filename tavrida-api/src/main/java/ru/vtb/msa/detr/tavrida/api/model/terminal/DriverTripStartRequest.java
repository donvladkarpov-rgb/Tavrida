package ru.vtb.msa.detr.tavrida.api.model.terminal;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;
import java.util.UUID;

public class DriverTripStartRequest {

    @Schema(
            description = "UUID сессии",
            example = "123e4567-e89b-42d3-a456-556642440000",
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    private UUID sessionId;
    @Schema(
            description = "UUID маршрута/пути",
            example = "123e4567-e89b-42d3-a456-556642440000",
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    private UUID routeGuid;
    @Schema(
            description = "Местное время",
            example = "2007-12-03T10:15:3",
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    private LocalDateTime terminalLocalStartTime;
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

    public UUID getRouteGuid() {
        return routeGuid;
    }

    public void setRouteGuid(UUID routeGuid) {
        this.routeGuid = routeGuid;
    }

    public LocalDateTime getTerminalLocalStartTime() {
        return terminalLocalStartTime;
    }

    public void setTerminalLocalStartTime(LocalDateTime terminalLocalStartTime) {
        this.terminalLocalStartTime = terminalLocalStartTime;
    }

    public String getTimeZoneOffset() {
        return timeZoneOffset;
    }

    public void setTimeZoneOffset(String timeZoneOffset) {
        this.timeZoneOffset = timeZoneOffset;
    }
}
