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

    @Schema(
            description = "Ид тарифа",
            example = "1",
            requiredMode = Schema.RequiredMode.NOT_REQUIRED
    )
    private Integer tariffTypeId;

    @Schema(
            description = "Номер банковской карты",
            example = "1234 5678 9012 3456",
            requiredMode = Schema.RequiredMode.NOT_REQUIRED
    )
    private String cardNumber;

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

    public Integer getTariffTypeId() {
        return tariffTypeId;
    }

    public void setTariffTypeId(Integer tariffTypeId) {
        this.tariffTypeId = tariffTypeId;
    }

    public String getCardNumber() {
        return cardNumber;
    }

    public void setCardNumber(String cardNumber) {
        this.cardNumber = cardNumber;
    }
}
