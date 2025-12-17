package ru.vtb.msa.detr.tavrida.api.model.terminal;

import ru.vtb.msa.detr.tavrida.api.model.trip.TripDto;

public class DriverTripResponse {
    private TripDto tripDto;
    private String status;
    private String message;

    public DriverTripResponse() {
    }

    public DriverTripResponse(TripDto tripDto, String status, String message) {
        this.tripDto = tripDto;
        this.status = status;
        this.message = message;
    }

    public TripDto getTripDto() {
        return tripDto;
    }

    public void setTripDto(TripDto tripId) {
        this.tripDto = tripDto;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

}
