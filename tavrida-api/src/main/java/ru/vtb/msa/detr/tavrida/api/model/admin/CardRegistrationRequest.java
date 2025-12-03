package ru.vtb.msa.detr.tavrida.api.model.admin;

import java.time.Instant;
import java.util.UUID;

public class CardRegistrationRequest {
    private UUID cardGuid;
    private Integer cardTypeId;
    private Long userId;
    private Integer uniqueTravelCount;
    private Integer maximumUniqueCount;
    private Integer availableTravelCount;
    private Instant expirationDate;

    // getters / setters
    public UUID getCardGuid() { return cardGuid; }
    public void setCardGuid(UUID cardGuid) { this.cardGuid = cardGuid; }

    public Integer getCardTypeId() { return cardTypeId; }
    public void setCardTypeId(Integer cardTypeId) { this.cardTypeId = cardTypeId; }

    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }

    public Integer getUniqueTravelCount() { return uniqueTravelCount; }
    public void setUniqueTravelCount(Integer uniqueTravelCount) { this.uniqueTravelCount = uniqueTravelCount; }

    public Integer getMaximumUniqueCount() { return maximumUniqueCount; }
    public void setMaximumUniqueCount(Integer maximumUniqueCount) { this.maximumUniqueCount = maximumUniqueCount; }

    public Integer getAvailableTravelCount() { return availableTravelCount; }
    public void setAvailableTravelCount(Integer availableTravelCount) { this.availableTravelCount = availableTravelCount; }

    public Instant getExpirationDate() { return expirationDate; }
    public void setExpirationDate(Instant expirationDate) { this.expirationDate = expirationDate; }
}