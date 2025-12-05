package ru.vtb.msa.detr.tavrida.api.model;

import java.time.Instant;
import java.util.UUID;

public class CardDto {
    private Long cardId;
    private UUID cardGuid;
    private CardTypeDto cardType;
    private UserDto user; // или только userId, если не нужен full user
    private TransportDto transport; // или только transportId, если не нужен full transport
    private Integer uniqueTravelCount;
    private Integer maximumUniqueCount;
    private Integer availableTravelCount;
    private Instant expirationDate;

    public CardDto() {}

    public CardDto(Long cardId, UUID cardGuid, CardTypeDto cardType, UserDto user,
                   TransportDto transport, Integer uniqueTravelCount, Integer maximumUniqueCount,
                   Integer availableTravelCount, Instant expirationDate) {
        this.cardId = cardId;
        this.cardGuid = cardGuid;
        this.cardType = cardType;
        this.user = user;
        this.transport = transport;
        this.uniqueTravelCount = uniqueTravelCount;
        this.maximumUniqueCount = maximumUniqueCount;
        this.availableTravelCount = availableTravelCount;
        this.expirationDate = expirationDate;
    }

    // Getters and Setters
    public Long getCardId() { return cardId; }
    public void setCardId(Long cardId) { this.cardId = cardId; }

    public UUID getCardGuid() { return cardGuid; }
    public void setCardGuid(UUID cardGuid) { this.cardGuid = cardGuid; }

    public CardTypeDto getCardType() { return cardType; }
    public void setCardType(CardTypeDto cardType) { this.cardType = cardType; }

    public UserDto getUser() { return user; }
    public void setUser(UserDto user) { this.user = user; }

    public TransportDto getTransport() { return transport; }
    public void setTransport(TransportDto transport) { this.transport = transport; }

    public Integer getUniqueTravelCount() { return uniqueTravelCount; }
    public void setUniqueTravelCount(Integer uniqueTravelCount) { this.uniqueTravelCount = uniqueTravelCount; }

    public Integer getMaximumUniqueCount() { return maximumUniqueCount; }
    public void setMaximumUniqueCount(Integer maximumUniqueCount) { this.maximumUniqueCount = maximumUniqueCount; }

    public Integer getAvailableTravelCount() { return availableTravelCount; }
    public void setAvailableTravelCount(Integer availableTravelCount) { this.availableTravelCount = availableTravelCount; }

    public Instant getExpirationDate() { return expirationDate; }
    public void setExpirationDate(Instant expirationDate) { this.expirationDate = expirationDate; }
}