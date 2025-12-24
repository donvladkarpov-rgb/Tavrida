package ru.vtb.msa.detr.tavrida.api.model.cashier;

import java.util.UUID;

public class CardInitResponse {
    private UUID cardUuid;
    private String expirationDate;

    public CardInitResponse() {
    }

    public CardInitResponse(UUID cardUuid, String expirationDate) {
        this.cardUuid = cardUuid;
        this.expirationDate = expirationDate;
    }

    public UUID getCardUuid() {
        return cardUuid;
    }

    public void setCardUuid(UUID cardUuid) {
        this.cardUuid = cardUuid;
    }

    public String getExpirationDate() {
        return expirationDate;
    }

    public void setExpirationDate(String expirationDate) {
        this.expirationDate = expirationDate;
    }
}
