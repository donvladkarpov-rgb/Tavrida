package ru.vtb.msa.detr.tavrida.core.model;

import jakarta.persistence.*;
import java.math.BigDecimal;
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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "transport_id")
    private Transport transport;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tariff_type_id") // ← НОВОЕ ПОЛЕ
    private TariffType tariffType;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pan_id")
    private CardPanHash cardPanHash;

    @Column(name = "unique_travel_count", nullable = false)
    private Integer uniqueTravelCount;

    @Column(name = "maximum_unique_count", nullable = false)
    private Integer maximumUniqueCount;

    @Column(name = "available_travel_count", nullable = false)
    private Integer availableTravelCount;

    @Column(name = "expiration_rides_package") // ← НОВОЕ ПОЛЕ
    private Instant expirationRidesPackage;

    @Column(name = "unlimited_until_date") // ← НОВОЕ ПОЛЕ
    private Instant unlimitedUntilDate;

    @Column(name = "wallet_balance") // ← НОВОЕ ПОЛЕ
    private BigDecimal walletBalance;

    @Column(name = "issue_date", nullable = false) // ← НОВОЕ ПОЛЕ
    private Instant issueDate;

    @Column(name = "expiration_date", nullable = false) // ← теперь NOT NULL
    private Instant expirationDate;

    // Getters and setters

    public Long getCardId() { return cardId; }
    public void setCardId(Long cardId) { this.cardId = cardId; }

    public UUID getCardGuid() { return cardGuid; }
    public void setCardGuid(UUID cardGuid) { this.cardGuid = cardGuid; }

    public CardType getCardType() { return cardType; }
    public void setCardType(CardType cardType) { this.cardType = cardType; }

    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }

    public Transport getTransport() { return transport; }
    public void setTransport(Transport transport) { this.transport = transport; }

    public TariffType getTariffType() { return tariffType; }
    public void setTariffType(TariffType tariffType) { this.tariffType = tariffType; }

    public Integer getUniqueTravelCount() { return uniqueTravelCount; }
    public void setUniqueTravelCount(Integer uniqueTravelCount) { this.uniqueTravelCount = uniqueTravelCount; }

    public Integer getMaximumUniqueCount() { return maximumUniqueCount; }
    public void setMaximumUniqueCount(Integer maximumUniqueCount) { this.maximumUniqueCount = maximumUniqueCount; }

    public Integer getAvailableTravelCount() { return availableTravelCount; }
    public void setAvailableTravelCount(Integer availableTravelCount) { this.availableTravelCount = availableTravelCount; }

    public Instant getExpirationRidesPackage() { return expirationRidesPackage; }
    public void setExpirationRidesPackage(Instant expirationRidesPackage) { this.expirationRidesPackage = expirationRidesPackage; }

    public Instant getUnlimitedUntilDate() { return unlimitedUntilDate; }
    public void setUnlimitedUntilDate(Instant unlimitedUntilDate) { this.unlimitedUntilDate = unlimitedUntilDate; }

    public BigDecimal getWalletBalance() { return walletBalance; }
    public void setWalletBalance(BigDecimal walletBalance) { this.walletBalance = walletBalance; }

    public Instant getIssueDate() { return issueDate; }
    public void setIssueDate(Instant issueDate) { this.issueDate = issueDate; }

    public Instant getExpirationDate() { return expirationDate; }
    public void setExpirationDate(Instant expirationDate) { this.expirationDate = expirationDate; }

    public Long getUserId() {
        return user != null ? user.getUserId() : null;
    }

    public CardPanHash getCardPanHash() {
        return cardPanHash;
    }

    public void setCardPanHash(CardPanHash cardPanHash) {
        this.cardPanHash = cardPanHash;
    }

}