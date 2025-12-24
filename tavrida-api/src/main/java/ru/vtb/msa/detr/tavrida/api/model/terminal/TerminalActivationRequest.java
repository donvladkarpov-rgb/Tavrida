package ru.vtb.msa.detr.tavrida.api.model.terminal;

import io.swagger.v3.oas.annotations.media.Schema;

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
        description = "Временная метка с терминала",
        example = "2025-12-23T21:52:08.754+0300",
        requiredMode = Schema.RequiredMode.REQUIRED
    )
    private String terminalActivationStartTime;

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

    public String getTerminalActivationStartTime() {
        return terminalActivationStartTime;
    }

    public void setTerminalActivationStartTime(String terminalActivationStartTime) {
        this.terminalActivationStartTime = terminalActivationStartTime;
    }
}