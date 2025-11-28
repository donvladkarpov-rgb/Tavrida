package ru.vtb.msa.detr.tavrida.api.model;

import java.util.UUID;

public class BlackListDto {
    private UUID cardGuid;

    public BlackListDto() {}

    public BlackListDto(UUID cardGuid) {
        this.cardGuid = cardGuid;
    }

    public UUID getCardGuid() { return cardGuid; }
    public void setCardGuid(UUID cardGuid) { this.cardGuid = cardGuid; }
}