package ru.vtb.msa.detr.tavrida.api.model.stop;

import ru.vtb.msa.detr.tavrida.api.model.carrier.FareZoneDto;
import java.io.Serializable;
import java.time.Instant;

public class StopsZoneMapDto implements Serializable {

    private Long id;
    private TransportStopDto stop;
    private FareZoneDto zone;
    private Instant createdAt;
    private Instant updatedAt;

    public StopsZoneMapDto() {}

    // Геттеры и сеттеры
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public TransportStopDto getStop() {
        return stop;
    }

    public void setStop(TransportStopDto stop) {
        this.stop = stop;
    }

    public FareZoneDto getZone() {
        return zone;
    }

    public void setZone(FareZoneDto zone) {
        this.zone = zone;
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