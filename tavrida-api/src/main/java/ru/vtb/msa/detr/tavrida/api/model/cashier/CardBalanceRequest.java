package ru.vtb.msa.detr.tavrida.api.model.cashier;

import java.util.UUID;

public class CardBalanceRequest {
    private UUID sessionId;
    private UUID cardUuid;
    private Integer currentTripsCount;

    public CardBalanceRequest() {
    }

    public CardBalanceRequest(Integer currentTripsCount, UUID sessionId, UUID cardUuid) {
        this.sessionId = sessionId;
        this.cardUuid = cardUuid;
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

    public Integer getCurrentTripsCount() {
        return currentTripsCount;
    }

    public void setCurrentTripsCount(Integer currentTripsCount) {
        this.currentTripsCount = currentTripsCount;
    }
}
