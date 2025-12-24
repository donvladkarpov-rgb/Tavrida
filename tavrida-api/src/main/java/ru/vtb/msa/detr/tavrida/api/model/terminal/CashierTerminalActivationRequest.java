package ru.vtb.msa.detr.tavrida.api.model.terminal;

import io.swagger.v3.oas.annotations.media.Schema;

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
        description = "Временная метка с терминала",
        example = "2025-12-23T21:52:08.754+0300",
        requiredMode = Schema.RequiredMode.REQUIRED
    )
    private String activationTerminalStartTime;

    // getters/setters
    public UUID getTerminalGuid() {
        return terminalGuid;
    }

    public void setTerminalGuid(UUID terminalGuid) {
        this.terminalGuid = terminalGuid;
    }

    public UUID getCardGuid() {
        return cardGuid;
    }

    public void setCardGuid(UUID cardGuid) {
        this.cardGuid = cardGuid;
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

    public String getActivationTerminalStartTime() {
        return activationTerminalStartTime;
    }

    public void setActivationTerminalStartTime(String activationTerminalStartTime) {
        this.activationTerminalStartTime = activationTerminalStartTime;
    }
}
