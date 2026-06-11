package ru.vtb.msa.detr.tavrida.api.model.cashier;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.UUID;

public class CardPurchaseRequest {
    @Schema(
        description = "UUID идентификатор сессии",
        example = "123e4567-e89b-42d3-a456-756642440001",
        requiredMode = Schema.RequiredMode.REQUIRED
    )
    private UUID sessionId;
    @Schema(
        description = "UUID идентификатор транзакции",
        example = "123e4567-e89b-42d3-a456-756642440001",
        requiredMode = Schema.RequiredMode.REQUIRED
    )
    private UUID transactionId;
    @Schema(
        description = "UUID идентификатор карты",
        example = "123e4567-e89b-42d3-a456-756642440001",
        requiredMode = Schema.RequiredMode.REQUIRED
    )
    private UUID cardUuid;
    @Schema(
        description = "Количество купленых поездок",
        example = "100",
        requiredMode = Schema.RequiredMode.REQUIRED
    )
    private Integer tripsCount;
    @Schema(
        description = "Текущий баланс карты (со стороны терминала)",
        example = "100",
        requiredMode = Schema.RequiredMode.REQUIRED
    )
    private Integer currentTripsCount;

    @Schema(
        description = "Временная метка с терминала",
        example = "2025-12-23T21:52:08.754+0300",
        requiredMode = Schema.RequiredMode.REQUIRED
    )
    private String terminalStartTime;

    public CardPurchaseRequest() {
    }

    public CardPurchaseRequest(Integer currentTripsCount, Integer tripsCount, UUID cardUuid, UUID sessionId) {
        this.tripsCount = tripsCount;
        this.cardUuid = cardUuid;
        this.sessionId = sessionId;
        this.currentTripsCount = currentTripsCount;
    }

    public UUID getSessionId() {
        return sessionId;
    }

    public void setSessionId(UUID sessionId) {
        this.sessionId = sessionId;
    }

    public UUID getCardUuid() {
        return cardUuid;
    }

    public void setCardUuid(UUID cardUuid) {
        this.cardUuid = cardUuid;
    }

    public Integer getTripsCount() {
        return tripsCount;
    }

    public void setTripsCount(Integer tripsCount) {
        this.tripsCount = tripsCount;
    }

    public Integer getCurrentTripsCount() {
        return currentTripsCount;
    }

    public void setCurrentTripsCount(Integer currentTripsCount) {
        this.currentTripsCount = currentTripsCount;
    }

    public String getTerminalStartTime() {
        return terminalStartTime;
    }

    public void setTerminalStartTime(String terminalStartTime) {
        this.terminalStartTime = terminalStartTime;
    }
}
