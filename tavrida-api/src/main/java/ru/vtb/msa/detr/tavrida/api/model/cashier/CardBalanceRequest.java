package ru.vtb.msa.detr.tavrida.api.model.cashier;

import java.util.UUID;

public class CardBalanceRequest {
    private UUID sessionId;
    private UUID cardUuid;

    public CardBalanceRequest() {
    }

    public CardBalanceRequest(UUID sessionId, UUID cardUuid) {
        this.sessionId = sessionId;
        this.cardUuid = cardUuid;
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
}
