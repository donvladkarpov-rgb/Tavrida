package ru.vtb.msa.detr.tavrida.api.model;

import java.time.Instant;
import java.util.UUID;

public class UserSessionDto {

    private UUID sessionId;
    private UserDto user;
    private TerminalDto terminal;
    private CardDto card;
    private Instant startedAt;
    private Instant startedAtLocal;
    private Instant closedAt;
    private Instant closedAtLocal;
    private Instant expirationTime;

    public UUID getSessionId() {
        return sessionId;
    }

    public void setSessionId(UUID sessionId) {
        this.sessionId = sessionId;
    }

    public UserDto getUser() {
        return user;
    }

    public void setUser(UserDto user) {
        this.user = user;
    }

    public TerminalDto getTerminal() {
        return terminal;
    }

    public void setTerminal(TerminalDto terminal) {
        this.terminal = terminal;
    }

    public CardDto getCard() {
        return card;
    }

    public void setCard(CardDto card) {
        this.card = card;
    }

    public Instant getStartedAt() {
        return startedAt;
    }

    public void setStartedAt(Instant startedAt) {
        this.startedAt = startedAt;
    }

    public Instant getStartedAtLocal() {
        return startedAtLocal;
    }

    public void setStartedAtLocal(Instant startedAtLocal) {
        this.startedAtLocal = startedAtLocal;
    }

    public Instant getClosedAt() {
        return closedAt;
    }

    public void setClosedAt(Instant closedAt) {
        this.closedAt = closedAt;
    }

    public Instant getClosedAtLocal() {
        return closedAtLocal;
    }

    public void setClosedAtLocal(Instant closedAtLocal) {
        this.closedAtLocal = closedAtLocal;
    }

    public Instant getExpirationTime() {
        return expirationTime;
    }

    public void setExpirationTime(Instant expirationTime) {
        this.expirationTime = expirationTime;
    }
}