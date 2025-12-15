package ru.vtb.msa.detr.tavrida.api.model.stop;

import java.io.Serializable;
import java.time.Instant;

public class TransportStopDto implements Serializable {
    private Long stopId;
    private Long fareZoneId;
    private String stopCode;
    private String stopName;
    private String stopAddress;
    private String geoLocation;
    private String description;
    private Boolean isActive;
    private Instant createdAt;
    private Instant updatedAt;

    public TransportStopDto() {}

    public Long getStopId() {
        return stopId;
    }

    public void setStopId(Long stopId) {
        this.stopId = stopId;
    }

    public Long getFareZoneId() {
        return fareZoneId;
    }

    public void setFareZoneId(Long fareZoneId) {
        this.fareZoneId = fareZoneId;
    }

    public String getStopCode() {
        return stopCode;
    }

    public void setStopCode(String stopCode) {
        this.stopCode = stopCode;
    }

    public String getStopName() {
        return stopName;
    }

    public void setStopName(String stopName) {
        this.stopName = stopName;
    }

    public String getStopAddress() {
        return stopAddress;
    }

    public void setStopAddress(String stopAddress) {
        this.stopAddress = stopAddress;
    }

    public String getGeoLocation() {
        return geoLocation;
    }

    public void setGeoLocation(String geoLocation) {
        this.geoLocation = geoLocation;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Boolean getIsActive() {
        return isActive;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Instant updatedAt) {
        this.updatedAt = updatedAt;
    }
}