package ru.vtb.msa.detr.tavrida.api.model.terminal;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;
import java.util.UUID;

public class CashierTerminalActivationRequest {
    @Schema(
            description = "Гуид терминала",
            example = "123e4567-e89b-42d3-a456-556642440001"
    )
    private UUID terminalGuid;
    @Schema(
            description = "Карта активации терминала",
            example = "123e4567-e89b-42d3-a456-556642440000",
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    private UUID cardGuid;
    @Schema(
            description = "Номер терминала",
            example = "D1234"
    )
    private String terminalNumber;
    @Schema(
            description = "Заводской номер терминала",
            example = "1234-33-2233",
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    private String terminalSerialNumber;
    @Schema(
            description = "Идентификаторо перевозчика",
            example = "123"
    )
    private Long carrierId;
    @Schema(
            description = "Местное время",
            example = "2007-12-03T10:15:3",
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    private LocalDateTime activationLocalStartTime;
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

    public LocalDateTime getActivationLocalStartTime() {
        return activationLocalStartTime;
    }

    public void setActivationLocalStartTime(LocalDateTime activationLocalStartTime) {
        this.activationLocalStartTime = activationLocalStartTime;
    }

    public String getTimeZoneOffset() {
        return timeZoneOffset;
    }

    public void setTimeZoneOffset(String timeZoneOffset) {
        this.timeZoneOffset = timeZoneOffset;
    }
}
