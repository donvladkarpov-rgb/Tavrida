package ru.vtb.msa.detr.tavrida.api.model.terminal;

import io.swagger.v3.oas.annotations.media.Schema;

public class DriverTripStopRequest {
    @Schema(
        description = "Номер рейса",
        example = "1234",
        requiredMode = Schema.RequiredMode.REQUIRED
    )
    private Long tripId;

    @Schema(
        description = "Временная метка с терминала",
        example = "2025-12-23T21:52:08.754+0300",
        requiredMode = Schema.RequiredMode.REQUIRED
    )
    private String tripStopTime;

    public Long getTripId() {
        return tripId;
    }

    public void setTripId(Long tripId) {
        this.tripId = tripId;
    }

    public String getTripStopTime() {
        return tripStopTime;
    }

    public void setTripStopTime(String tripStopTime) {
        this.tripStopTime = tripStopTime;
    }
}
