package ru.vtb.msa.detr.tavrida.core.model;

import jakarta.persistence.*;

@Entity
@Table(name = "card_types")
public class CardType {

    @Id
    @Column(name = "card_type_id")
    private Integer cardTypeId;

    @Column(name = "card_type_name")
    private String cardTypeName;

    // --- getters/setters ---
    public Integer getCardTypeId() { return cardTypeId; }
    public void setCardTypeId(Integer cardTypeId) { this.cardTypeId = cardTypeId; }

    public String getCardTypeName() { return cardTypeName; }
    public void setCardTypeName(String cardTypeName) { this.cardTypeName = cardTypeName; }
}