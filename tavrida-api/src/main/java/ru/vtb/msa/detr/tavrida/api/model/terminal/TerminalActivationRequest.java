package ru.vtb.msa.detr.tavrida.api.model.terminal;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;
import java.util.UUID;

public class TerminalActivationRequest {
    @Schema(
            description = "UUID терминала",
            example = "123e4567-e89b-42d3-a456-556642440000",
            requiredMode = Schema.RequiredMode.NOT_REQUIRED
    )
    private UUID terminalGuid;
    @Schema(
            description = "UUID транспорта",
            example = "123e4567-e89b-42d3-a456-556642440000",
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    private UUID transportGuid;
    @Schema(
            description = "UUID карты",
            example = "123e4567-e89b-42d3-a456-556642440000",
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    private UUID cardGuid; // Новая карта активации
    @Schema(
            description = "Номер терминала",
            example = "A1234",
            requiredMode = Schema.RequiredMode.NOT_REQUIRED
    )
    private String terminalNumber;
    @Schema(
            description = "Заводская серия терминала",
            example = "Любая строка",
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    private String terminalSerialNumber;
    @Schema(
            description = "ID Перевозчика",
            example = "12345",
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    private Long carrierId;
    @Schema(
            description = "Местное время",
            example = "2007-12-03T10:15:3",
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    private LocalDateTime terminalActivationStartTime;
    @Schema(
            description = "Часовой пояс местного времени",
            example = "Z - UTC, +h, +hh, +hh:mm, -h, -hh, -hh:mm",
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    private String timeZoneOffset;

    // getters/setters
    public UUID getTerminalGuid() { return terminalGuid; }
    public void setTerminalGuid(UUID terminalGuid) { this.terminalGuid = terminalGuid; }

    public UUID getCardGuid() { return cardGuid; }
    public void setCardGuid(UUID cardGuid) { this.cardGuid = cardGuid; }

    public UUID getTransportGuid() {
        return transportGuid;
    }

    public void setTransportGuid(UUID transportGuid) {
        this.transportGuid = transportGuid;
    }

    public String getTerminalNumber() {
        return terminalNumber;
    }

    public void setTerminalNumber(String terminalNumber) {
        this.terminalNumber = terminalNumber;
    }

    public String getTerminalSerialNumber() {
        return terminalSerialNumber;
    }

    public void setTerminalSerialNumber(String terminalSerialNumber) {
        this.terminalSerialNumber = terminalSerialNumber;
    }

    public Long getCarrierId() {
        return carrierId;
    }

    public void setCarrierId(Long carrierId) {
        this.carrierId = carrierId;
    }

    public LocalDateTime getTerminalActivationStartTime() {
        return terminalActivationStartTime;
    }

    public void setTerminalActivationStartTime(LocalDateTime terminalActivationStartTime) {
        this.terminalActivationStartTime = terminalActivationStartTime;
    }

    public String getTimeZoneOffset() {
        return timeZoneOffset;
    }

    public void setTimeZoneOffset(String timeZoneOffset) {
        this.timeZoneOffset = timeZoneOffset;
    }
}