package ru.vtb.msa.detr.tavrida.api.model.admin;

import java.util.UUID;

public class UserRegistrationRequest {
    private UUID sessionId;
    private String userFio;
    private Integer userRoleId;
    private Long carrierId; // может быть null, если роль не требует привязки к перевозчику
    private String password; // или passwordHash, в зависимости от политики

    // getters / setters
    public String getUserFio() { return userFio; }
    public void setUserFio(String userFio) { this.userFio = userFio; }

    public Integer getUserRoleId() { return userRoleId; }
    public void setUserRoleId(Integer userRoleId) { this.userRoleId = userRoleId; }

    public Long getCarrierId() { return carrierId; }
    public void setCarrierId(Long carrierId) { this.carrierId = carrierId; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public UUID getSessionId() {
        return sessionId;
    }

    public void setSessionId(UUID sessionId) {
        this.sessionId = sessionId;
    }
}