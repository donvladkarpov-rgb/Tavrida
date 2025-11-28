package ru.vtb.msa.detr.tavrida.core.model;

import jakarta.persistence.*;
import java.util.UUID;

@Entity
@Table(name = "black_list")
public class BlackList {

    @Id
    @Column(name = "card_guid", nullable = false, unique = true)
    private UUID cardGuid;

    // --- getters/setters ---
    public UUID getCardGuid() { return cardGuid; }
    public void setCardGuid(UUID cardGuid) { this.cardGuid = cardGuid; }
}