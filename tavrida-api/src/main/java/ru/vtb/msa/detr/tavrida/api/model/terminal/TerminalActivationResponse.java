package ru.vtb.msa.detr.tavrida.api.model.terminal;

public class TerminalActivationResponse {
    private boolean success;
    private String message;
    private Long terminalId;

    public TerminalActivationResponse(boolean success, String message) {
        this.success = success;
        this.message = message;
    }

    public TerminalActivationResponse(boolean success, String message, Long terminalId) {
        this.success = success;
        this.message = message;
        this.terminalId = terminalId;
    }

    // getters
    public boolean isSuccess() { return success; }
    public String getMessage() { return message; }
    public Long getTerminalId() { return terminalId; }
}