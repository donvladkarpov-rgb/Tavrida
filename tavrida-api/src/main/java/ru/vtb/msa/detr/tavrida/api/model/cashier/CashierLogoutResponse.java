package ru.vtb.msa.detr.tavrida.api.model.cashier;

import ru.vtb.msa.detr.tavrida.api.model.UserDto;

import java.time.LocalDateTime;
import java.util.UUID;

public class CashierLogoutResponse {

    private boolean success;
    private String message;
    private UUID sessionId;
    private UUID cashierCardGuid;
    private LocalDateTime startTime;
    private UserDto cashier;

    public CashierLogoutResponse(boolean success, String message) {
        this.success = success;
        this.message = message;
    }

    public CashierLogoutResponse(
            boolean success,
            String message,
            UUID sessionId,
            UUID cashierCardGuid,
            UUID transportGuid,
            LocalDateTime startTime) {
        this.success = success;
        this.message = message;
        this.sessionId = sessionId;
        this.cashierCardGuid = cashierCardGuid;
        this.startTime = startTime;
    }

    public CashierLogoutResponse(
            boolean success,
            String message,
            UUID sessionId,
            UUID cashierCardGuid,
            LocalDateTime startTime,
            UserDto cashier) {
        this.success = success;
        this.message = message;
        this.sessionId = sessionId;
        this.cashierCardGuid = cashierCardGuid;
        this.startTime = startTime;
        this.cashier = cashier;
    }

    // Getters & Setters
    public boolean isSuccess() { return success; }
    public void setSuccess(boolean success) { this.success = success; }

    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }

    public UUID getSessionId() { return sessionId; }
    public void setSessionId(UUID sessionId) { this.sessionId = sessionId; }

    public UUID getCashierCardGuid() {
        return cashierCardGuid;
    }

    public void setCashierCardGuid(UUID cashierCardGuid) {
        this.cashierCardGuid = cashierCardGuid;
    }

    public UserDto getCashier() {
        return cashier;
    }

    public void setCashier(UserDto cashier) {
        this.cashier = cashier;
    }

    public LocalDateTime getStartTime() { return startTime; }
    public void setStartTime(LocalDateTime startTime) { this.startTime = startTime; }

}
