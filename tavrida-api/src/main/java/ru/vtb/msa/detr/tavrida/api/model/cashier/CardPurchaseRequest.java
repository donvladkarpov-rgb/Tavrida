package ru.vtb.msa.detr.tavrida.api.model.cashier;

import java.util.UUID;

public class CardPurchaseRequest {
    private UUID sessionId;
    private UUID cardUuid;
    private Integer tripsCount;

    public CardPurchaseRequest() {
    }

    public CardPurchaseRequest(Integer tripsCount, UUID cardUuid, UUID sessionId) {
        this.tripsCount = tripsCount;
        this.cardUuid = cardUuid;
        this.sessionId = sessionId;
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
}
