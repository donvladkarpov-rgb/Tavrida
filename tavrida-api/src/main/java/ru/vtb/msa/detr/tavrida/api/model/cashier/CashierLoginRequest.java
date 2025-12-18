package ru.vtb.msa.detr.tavrida.api.model.cashier;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;
import java.util.UUID;

public class CashierLoginRequest {
@Schema(
            description = "UUID идентификатор карты",
            example = "123e4567-e89b-42d3-a456-756642440001",
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    private UUID cardUuid;
    @Schema(
            description = "Хэш пароля пользователя",
            example = "FIRST_PASSWORD",
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    private String hashPassword;
//    @Schema(
//            description = "Ид терминала",
//            example = "1"
//    )
//    private Long terminalId;
    @Schema(
            description = "UUID идентификатор терминала",
            example = "3f2dd185-0540-4d82-ad26-e6a181cb1893",
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    private UUID terminalGuid;
    @Schema(
            description = "Местное время",
            example = "2007-12-03T10:15:3"
    )
    private LocalDateTime loginLocalStartTime;
    @Schema(
            description = "Часовой пояс местного времени",
            example = "Z - UTC, +h, +hh, +hh:mm, -h, -hh, -hh:mm",
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    private String timeZoneOffset;

    public UUID getTerminalGuid() {
        return terminalGuid;
    }

    public void setTerminalGuid(UUID terminalGuid) {
        this.terminalGuid = terminalGuid;
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

//    public Long getTerminalId() {
//        return terminalId;
//    }
//
//    public void setTerminalId(Long terminalId) {
//        this.terminalId = terminalId;
//    }

    public LocalDateTime getLoginLocalStartTime() {
        return loginLocalStartTime;
    }

    public void setLoginLocalStartTime(LocalDateTime loginLocalStartTime) {
        this.loginLocalStartTime = loginLocalStartTime;
    }

    public String getTimeZoneOffset() {
        return timeZoneOffset;
    }

    public void setTimeZoneOffset(String timeZoneOffset) {
        this.timeZoneOffset = timeZoneOffset;
    }
}
