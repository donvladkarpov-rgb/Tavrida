package ru.vtb.msa.detr.tavrida.api.model.terminal;

import ru.vtb.msa.detr.tavrida.api.model.UserDto;

import java.time.LocalDateTime;
import java.util.UUID;

public class DriverSessionResponse {
    private boolean success;
    private String message;
    private UUID sessionId;
    private UUID driverCardGuid;
    private UUID transportGuid;
    private LocalDateTime startTime;
    private UserDto driver;

    public DriverSessionResponse(boolean success, String message) {
        this.success = success;
        this.message = message;
    }

    public DriverSessionResponse(
            boolean success,
            String message,
            UUID sessionId,
            UUID driverCardGuid,
            UUID transportGuid,
            LocalDateTime startTime) {
        this.success = success;
        this.message = message;
        this.sessionId = sessionId;
        this.driverCardGuid = driverCardGuid;
        this.transportGuid = transportGuid;
        this.startTime = startTime;
    }

    public DriverSessionResponse(
            boolean success,
            String message,
            UUID sessionId,
            UUID driverCardGuid,
            UUID transportGuid,
            LocalDateTime startTime,
            UserDto driver) {
        this.success = success;
        this.message = message;
        this.sessionId = sessionId;
        this.driverCardGuid = driverCardGuid;
        this.transportGuid = transportGuid;
        this.startTime = startTime;
        this.driver = driver;
    }

    // Getters & Setters
    public boolean isSuccess() { return success; }
    public void setSuccess(boolean success) { this.success = success; }

    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }

    public UUID getSessionId() { return sessionId; }
    public void setSessionId(UUID sessionId) { this.sessionId = sessionId; }

    public UUID getDriverCardGuid() { return driverCardGuid; }
    public void setDriverCardGuid(UUID driverCardGuid) { this.driverCardGuid = driverCardGuid; }

    public UUID getTransportGuid() { return transportGuid; }
    public void setTransportGuid(UUID transportGuid) { this.transportGuid = transportGuid; }

    public LocalDateTime getStartTime() { return startTime; }
    public void setStartTime(LocalDateTime startTime) { this.startTime = startTime; }
}