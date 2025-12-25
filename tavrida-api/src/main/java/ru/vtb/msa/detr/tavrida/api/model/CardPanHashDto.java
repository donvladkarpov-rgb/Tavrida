package ru.vtb.msa.detr.tavrida.api.model;

import java.util.List;

public class CardPanHashDto {

    private Long panId;
    private String panHash;
    private List<CardDto> cards;

    // --- Constructors ---
    public CardPanHashDto() {}

    public CardPanHashDto(Long panId, String panHash, List<CardDto> cards) {
        this.panId = panId;
        this.panHash = panHash;
        this.cards = cards;
    }

    // --- Getters and Setters ---
    public Long getPanId() {
        return panId;
    }

    public void setPanId(Long panId) {
        this.panId = panId;
    }

    public String getPanHash() {
        return panHash;
    }

    public void setPanHash(String panHash) {
        this.panHash = panHash;
    }

     public List<CardDto> getCards() { return cards; }
     public void setCards(List<CardDto> cards) { this.cards = cards; }

}