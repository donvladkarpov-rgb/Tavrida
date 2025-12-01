package ru.vtb.msa.detr.tavrida.api.model.cashier;

import java.time.LocalDateTime;
import java.util.UUID;

public class CardInitResponse {
    private UUID cardUuid;
    private LocalDateTime expirationDate;

    public CardInitResponse() {
    }

    public CardInitResponse(UUID cardUuid, LocalDateTime expirationDate) {
        this.cardUuid = cardUuid;
        this.expirationDate = expirationDate;
    }

    public UUID getCardUuid() {
        return cardUuid;
    }

    public void setCardUuid(UUID cardUuid) {
        this.cardUuid = cardUuid;
    }

    public LocalDateTime getExpirationDate() {
        return expirationDate;
    }

    public void setExpirationDate(LocalDateTime expirationDate) {
        this.expirationDate = expirationDate;
    }
}
