package ru.vtb.msa.detr.tavrida.api.model;

import java.time.LocalDateTime;
import java.util.UUID;

public class UserSessionDto {
    private UUID sessionId;
    private Long userId;
    private LocalDateTime expirationTime;

    public UserSessionDto() {}

    public UserSessionDto(UUID sessionId, Long userId, LocalDateTime expirationTime) {
        this.sessionId = sessionId;
        this.userId = userId;
        this.expirationTime = expirationTime;
    }

    // Геттеры и сеттеры
    public UUID getSessionId() { return sessionId; }
    public void setSessionId(UUID sessionId) { this.sessionId = sessionId; }

    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }

    public LocalDateTime getExpirationTime() { return expirationTime; }
    public void setExpirationTime(LocalDateTime expirationTime) { this.expirationTime = expirationTime; }
}