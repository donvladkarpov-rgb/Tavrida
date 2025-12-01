package ru.vtb.msa.detr.tavrida.api.model.cashier;

import java.util.UUID;

public class CardPurchaseResponse {
    private UUID cardUuid;
    private Integer newTripsCount;
    private Long paymentId;

    public CardPurchaseResponse() {
    }

    public CardPurchaseResponse(UUID cardUuid, Integer newTripsCount, Long paymentId) {
        this.cardUuid = cardUuid;
        this.newTripsCount = newTripsCount;
        this.paymentId = paymentId;
    }

    public UUID getCardUuid() {
        return cardUuid;
    }

    public void setCardUuid(UUID cardUuid) {
        this.cardUuid = cardUuid;
    }

    public Integer getNewTripsCount() {
        return newTripsCount;
    }

    public void setNewTripsCount(Integer newTripsCount) {
        this.newTripsCount = newTripsCount;
    }

    public Long getPaymentId() {
        return paymentId;
    }

    public void setPaymentId(Long paymentId) {
        this.paymentId = paymentId;
    }
}
