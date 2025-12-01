package ru.vtb.msa.detr.tavrida.api.model.cashier;

public class CardBalanceResponse {
    private Integer tripsCount;

    public CardBalanceResponse() {
    }

    public CardBalanceResponse(Integer tripsCount) {
        this.tripsCount = tripsCount;
    }

    public Integer getTripsCount() {
        return tripsCount;
    }

    public void setTripsCount(Integer tripsCount) {
        this.tripsCount = tripsCount;
    }
}
