package ru.vtb.msa.detr.tavrida.api.model;

import java.time.Instant;
import java.util.UUID;

public class UserSessionDto {

    private UUID sessionId;
    private UserDto user;
    private TerminalDto terminal;
    private CardDto card;
    private Instant startedAtServer;
    private Instant startedAtTerminal;
    private Instant closedAtServer;
    private Instant closedAtTerminal;
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

    public Instant getStartedAtServer() {
        return startedAtServer;
    }

    public void setStartedAtServer(Instant startedAtServer) {
        this.startedAtServer = startedAtServer;
    }

    public Instant getStartedAtTerminal() {
        return startedAtTerminal;
    }

    public void setStartedAtTerminal(Instant startedAtTerminal) {
        this.startedAtTerminal = startedAtTerminal;
    }

    public Instant getClosedAtServer() {
        return closedAtServer;
    }

    public void setClosedAtServer(Instant closedAtServer) {
        this.closedAtServer = closedAtServer;
    }

    public Instant getClosedAtTerminal() {
        return closedAtTerminal;
    }

    public void setClosedAtTerminal(Instant closedAtTerminal) {
        this.closedAtTerminal = closedAtTerminal;
    }

    public Instant getExpirationTime() {
        return expirationTime;
    }

    public void setExpirationTime(Instant expirationTime) {
        this.expirationTime = expirationTime;
    }
}