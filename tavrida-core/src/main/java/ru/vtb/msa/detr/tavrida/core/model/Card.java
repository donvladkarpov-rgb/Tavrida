package ru.vtb.msa.detr.tavrida.core.model;

import jakarta.persistence.*;
import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "cards")
public class Card {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "card_id")
    private Long cardId;

    @Column(name = "card_guid", nullable = false, unique = true)
    private UUID cardGuid;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "card_type_id", nullable = false)
    private CardType cardType;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @Column(name = "unique_travel_count", nullable = false)
    private Integer uniqueTravelCount;

    @Column(name = "maximum_unique_count", nullable = false)
    private Integer maximumUniqueCount;

    @Column(name = "available_travel_count", nullable = false)
    private Integer availableTravelCount;

    @Column(name = "expiration_date")
    private Instant expirationDate;

    // --- getters/setters ---
    public Long getCardId() { return cardId; }
    public void setCardId(Long cardId) { this.cardId = cardId; }

    public UUID getCardGuid() { return cardGuid; }
    public void setCardGuid(UUID cardGuid) { this.cardGuid = cardGuid; }

    public CardType getCardType() { return cardType; }
    public void setCardType(CardType cardType) { this.cardType = cardType; }

    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }

    public Integer getUniqueTravelCount() { return uniqueTravelCount; }
    public void setUniqueTravelCount(Integer uniqueTravelCount) { this.uniqueTravelCount = uniqueTravelCount; }

    public Integer getMaximumUniqueCount() { return maximumUniqueCount; }
    public void setMaximumUniqueCount(Integer maximumUniqueCount) { this.maximumUniqueCount = maximumUniqueCount; }

    public Integer getAvailableTravelCount() { return availableTravelCount; }
    public void setAvailableTravelCount(Integer availableTravelCount) { this.availableTravelCount = availableTravelCount; }

    public Instant getExpirationDate() { return expirationDate; }
    public void setExpirationDate(Instant expirationDate) { this.expirationDate = expirationDate; }
}