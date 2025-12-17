package ru.vtb.msa.detr.tavrida.api.model.terminal;

import ru.vtb.msa.detr.tavrida.api.model.TerminalDto;


public class TerminalActivationResponse {
    private boolean success;
    private String message;
    private TerminalDto terminal;

    public TerminalActivationResponse(boolean success, String message) {
        this.success = success;
        this.message = message;
    }

    public TerminalActivationResponse(boolean success, String message, TerminalDto terminal) {
        this.success = success;
        this.message = message;
        this.terminal = terminal;
    }

    // getters
    public boolean isSuccess() { return success; }
    public String getMessage() { return message; }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public TerminalDto getTerminal() {
        return terminal;
    }

    public void setTerminal(TerminalDto terminal) {
        this.terminal = terminal;
    }
}