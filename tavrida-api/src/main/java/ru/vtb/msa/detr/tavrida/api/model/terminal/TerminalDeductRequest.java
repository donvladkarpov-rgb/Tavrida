package ru.vtb.msa.detr.tavrida.api.model.terminal;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;
import java.util.UUID;

public class TerminalDeductRequest {
    @Schema(
            description = "Ид рейса",
            example = "123456",
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    private Long tripId;
    @Schema(
            description = "UUID сессии",
            example = "123e4567-e89b-42d3-a456-556642440000",
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    private UUID sessionId;
    @Schema(
            description = "UUID идентификатор карты",
            example = "123e4567-e89b-42d3-a456-556642440000",
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    private UUID cardGuid;
    @Schema(
            description = "UUID идентификатор терминала",
            example = "3f2dd185-0540-4d82-ad26-e6a181cb1893",
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    private UUID terminalGuid;
    @Schema(
            description = "Оставшееся количество поездок на карте",
            example = "999",
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    private int terminalBalance;
    @Schema(
            description = "Местное время",
            example = "2007-12-03T10:15:3",
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    private LocalDateTime terminalDeductStartTime;
    @Schema(
            description = "Часовой пояс местного времени",
            example = "Z - UTC, +h, +hh, +hh:mm, -h, -hh, -hh:mm",
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    private String timeZoneOffset;


    // Конструктор по умолчанию (обязателен для Jackson)
    public TerminalDeductRequest() {}

    // Геттеры и сеттеры


    public UUID getSessionId() {
        return sessionId;
    }

    public void setSessionId(UUID sessionId) {
        this.sessionId = sessionId;
    }

    public UUID getCardGuid() { return cardGuid; }
    public void setCardGuid(UUID cardGuid) { this.cardGuid = cardGuid; }

    public UUID getTerminalGuid() { return terminalGuid; }
    public void setTerminalGuid(UUID terminalGuid) { this.terminalGuid = terminalGuid; }

    public int getTerminalBalance() { return terminalBalance; }
    public void setTerminalBalance(int terminalBalance) { this.terminalBalance = terminalBalance; }

    public LocalDateTime getTerminalDeductStartTime() {
        return terminalDeductStartTime;
    }

    public void setTerminalDeductStartTime(LocalDateTime terminalDeductStartTime) {
        this.terminalDeductStartTime = terminalDeductStartTime;
    }

    public String getTimeZoneOffset() {
        return timeZoneOffset;
    }

    public void setTimeZoneOffset(String timeZoneOffset) {
        this.timeZoneOffset = timeZoneOffset;
    }

    public Long getTripId() {
        return tripId;
    }

    public void setTripId(Long tripId) {
        this.tripId = tripId;
    }
}