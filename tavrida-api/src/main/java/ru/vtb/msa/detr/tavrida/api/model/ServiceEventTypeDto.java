package ru.vtb.msa.detr.tavrida.api.model;

public class ServiceEventTypeDto {
    private String eventType;
    private String eventTypeName;

    public ServiceEventTypeDto() {}

    public String getEventType() { return eventType; }
    public void setEventType(String eventType) { this.eventType = eventType; }

    public String getEventTypeName() { return eventTypeName; }
    public void setEventTypeName(String eventTypeName) { this.eventTypeName = eventTypeName; }
}