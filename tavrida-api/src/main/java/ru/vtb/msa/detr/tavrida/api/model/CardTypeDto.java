package ru.vtb.msa.detr.tavrida.api.model;

public class CardTypeDto {
    private Integer cardTypeId;
    private String cardTypeName;

    public CardTypeDto() {}

    public CardTypeDto(Integer cardTypeId, String cardTypeName) {
        this.cardTypeId = cardTypeId;
        this.cardTypeName = cardTypeName;
    }

    public Integer getCardTypeId() { return cardTypeId; }
    public void setCardTypeId(Integer cardTypeId) { this.cardTypeId = cardTypeId; }

    public String getCardTypeName() { return cardTypeName; }
    public void setCardTypeName(String cardTypeName) { this.cardTypeName = cardTypeName; }
}