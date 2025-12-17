package ru.vtb.msa.detr.tavrida.api.model.cashier;

import java.util.UUID;

public class CardPurchaseRequest {
    private UUID sessionId;
    private UUID cardUuid;
    private Integer tripsCount;
    private Integer currentTripsCount;

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
}
