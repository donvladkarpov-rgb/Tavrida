package ru.vtb.msa.detr.tavrida.api.model.terminal;

public class DriverTripResponse {
    private Long tripId;
    private String status;
    private String message;

    public DriverTripResponse() {
    }

    public DriverTripResponse(Long tripId, String status, String message) {
        this.tripId = tripId;
        this.status = status;
        this.message = message;
    }

    public Long getTripId() {
        return tripId;
    }

    public void setTripId(Long tripId) {
        this.tripId = tripId;
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
