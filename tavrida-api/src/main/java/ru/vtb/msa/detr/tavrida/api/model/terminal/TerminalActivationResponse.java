package ru.vtb.msa.detr.tavrida.api.model.terminal;

import java.util.UUID;

public class TerminalActivationResponse {
    private boolean success;
    private String message;
    private Long terminalId;
    private UUID terminalGuid;
    private String terminalNumber;
    private String terminalSerialNumber;


    public TerminalActivationResponse(boolean success, String message) {
        this.success = success;
        this.message = message;
    }

    public TerminalActivationResponse(boolean success, String message, Long terminalId) {
        this.success = success;
        this.message = message;
        this.terminalId = terminalId;
    }

    public TerminalActivationResponse(boolean success, String message, Long terminalId, UUID terminalGuid) {
        this.success = success;
        this.message = message;
        this.terminalId = terminalId;
        this.terminalGuid = terminalGuid;
    }

    public TerminalActivationResponse(boolean success, String message, Long terminalId, UUID terminalGuid, String terminalNumber) {
        this.success = success;
        this.message = message;
        this.terminalId = terminalId;
        this.terminalGuid = terminalGuid;
        this.terminalNumber = terminalNumber;
    }

    public TerminalActivationResponse(boolean success, String message, Long terminalId, UUID terminalGuid, String terminalNumber, String terminalSerialNumber) {
        this.success = success;
        this.message = message;
        this.terminalId = terminalId;
        this.terminalGuid = terminalGuid;
        this.terminalNumber = terminalNumber;
        this.terminalSerialNumber = terminalSerialNumber;
    }

    // getters
    public boolean isSuccess() { return success; }
    public String getMessage() { return message; }
    public Long getTerminalId() { return terminalId; }

    public UUID getTerminalGuid() {
        return terminalGuid;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setTerminalId(Long terminalId) {
        this.terminalId = terminalId;
    }

    public void setTerminalGuid(UUID terminalGuid) {
        this.terminalGuid = terminalGuid;
    }

    public String getTerminalNumber() {
        return terminalNumber;
    }

    public void setTerminalNumber(String terminalNumber) {
        this.terminalNumber = terminalNumber;
    }

    public String getTerminalSerialNumber() {
        return terminalSerialNumber;
    }

    public void setTerminalSerialNumber(String terminalSerialNumber) {
        this.terminalSerialNumber = terminalSerialNumber;
    }
}