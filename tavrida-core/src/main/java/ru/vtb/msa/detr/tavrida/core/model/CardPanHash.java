package ru.vtb.msa.detr.tavrida.core.model;

import jakarta.persistence.*;
import java.util.List;

@Entity
@Table(name = "card_pan_hashes")
public class CardPanHash {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "pan_id")
    private Long panId;

    @Column(name = "pan_hash", nullable = false, unique = true, length = 512)
    private String panHash;

     @OneToMany(mappedBy = "cardPanHash", fetch = FetchType.LAZY)
     private List<Card> cards;

    // --- Getters and Setters ---
    public Long getPanId() { return panId; }
    public void setPanId(Long panId) { this.panId = panId; }

    public String getPanHash() { return panHash; }
    public void setPanHash(String panHash) { this.panHash = panHash; }

    public List<Card> getCards() {
        return cards;
    }

    public void setCards(List<Card> cards) {
        this.cards = cards;
    }
}