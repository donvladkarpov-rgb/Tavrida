package ru.vtb.msa.detr.tavrida.core.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "user_sessions")
public class UserSession {

    @Id
    @Column(name = "session_id")
    private UUID sessionId;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(name = "expiration_time", nullable = false)
    private LocalDateTime expirationTime;

    // Constructors
    public UserSession() {}

    public UserSession(UUID sessionId, Long userId, LocalDateTime expirationTime) {
        this.sessionId = sessionId;
        this.userId = userId;
        this.expirationTime = expirationTime;
    }

    // Getters and Setters
    public UUID getSessionId() { return sessionId; }
    public void setSessionId(UUID sessionId) { this.sessionId = sessionId; }

    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }

    public LocalDateTime getExpirationTime() { return expirationTime; }
    public void setExpirationTime(LocalDateTime expirationTime) { this.expirationTime = expirationTime; }
}