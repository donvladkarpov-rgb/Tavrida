package ru.vtb.msa.detr.tavrida.api.model.terminal;

import ru.vtb.msa.detr.tavrida.api.model.UserSessionDto;
import ru.vtb.msa.detr.tavrida.api.model.trip.TripDto;

public class TerminalDeductResponse {
    private boolean success;
    private String message;
    private TripDto tripDto;
    private UserSessionDto sessionDto;

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public TripDto getTripDto() {
        return tripDto;
    }

    public void setTripDto(TripDto tripDto) {
        this.tripDto = tripDto;
    }

    public UserSessionDto getSessionDto() {
        return sessionDto;
    }

    public void setSessionDto(UserSessionDto sessionDto) {
        this.sessionDto = sessionDto;
    }
}
