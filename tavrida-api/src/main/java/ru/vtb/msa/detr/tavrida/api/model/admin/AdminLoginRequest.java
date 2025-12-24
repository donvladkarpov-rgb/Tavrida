package ru.vtb.msa.detr.tavrida.api.model.admin;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;
import java.util.UUID;

public class AdminLoginRequest {
    @Schema(
        description = "UUID идентификатор карты",
        example = "123e4567-e89b-42d3-a456-556642440000",
        requiredMode = Schema.RequiredMode.REQUIRED
    )
    private UUID cardUuid;
    @Schema(
        description = "Хэш пароля пользователя",
        example = "FIRST_PASSWORD",
        requiredMode = Schema.RequiredMode.REQUIRED
    )
    private String hashPassword;
    @Schema(
        description = "Ид терминала",
        example = "1",
        requiredMode = Schema.RequiredMode.NOT_REQUIRED
    )
    private Long terminalId;
    @Schema(
        description = "UUID идентификатор терминала",
        example = "3f2dd185-0540-4d82-ad26-e6a181cb1893",
        requiredMode = Schema.RequiredMode.REQUIRED
    )
    private UUID terminalGuid;

    @Schema(
        description = "Временная метка с терминала",
        example = "2025-12-23T21:52:08.754+0300",
        requiredMode = Schema.RequiredMode.REQUIRED
    )
    private String loginTerminalStartTime;

    public AdminLoginRequest(UUID cardUuid, String hashPassword, Long terminalId, UUID terminalGuid) {
        this.cardUuid = cardUuid;
        this.hashPassword = hashPassword;
        this.terminalId = terminalId;
        this.terminalGuid = terminalGuid;
    }

    public UUID getTerminalGuid() {
        return terminalGuid;
    }

    public void setTerminalGuid(UUID terminalGuid) {
        this.terminalGuid = terminalGuid;
    }

    public AdminLoginRequest() {
    }

    public AdminLoginRequest(UUID cardUuid, String hashPassword, Long terminalId) {
        this.cardUuid = cardUuid;
        this.hashPassword = hashPassword;
        this.terminalId = terminalId;
    }

    public UUID getCardUuid() {
        return cardUuid;
    }

    public void setCardUuid(UUID cardUuid) {
        this.cardUuid = cardUuid;
    }

    public String getHashPassword() {
        return hashPassword;
    }

    public void setHashPassword(String hashPassword) {
        this.hashPassword = hashPassword;
    }

    public Long getTerminalId() {
        return terminalId;
    }

    public void setTerminalId(Long terminalId) {
        this.terminalId = terminalId;
    }
}
