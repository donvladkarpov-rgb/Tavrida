package ru.vtb.msa.detr.tavrida.api.model;

import java.time.LocalDateTime;

public class ServiceEventDto {
    private Long serviceEventId;
    private LocalDateTime eventTime;
    private ServiceEventTypeDto serviceEventType;
    private Long userId;
    private Integer referenceTypeId;
    private Long referenceId;
    private String eventDetails;

    public ServiceEventDto() {}

    // Геттеры и сеттеры
    public Long getServiceEventId() { return serviceEventId; }
    public void setServiceEventId(Long serviceEventId) { this.serviceEventId = serviceEventId; }

    public LocalDateTime getEventTime() { return eventTime; }
    public void setEventTime(LocalDateTime eventTime) { this.eventTime = eventTime; }

    public ServiceEventTypeDto getServiceEventType() { return serviceEventType; }
    public void setServiceEventType(ServiceEventTypeDto serviceEventType) { this.serviceEventType = serviceEventType; }

    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }

    public Integer getReferenceTypeId() { return referenceTypeId; }
    public void setReferenceTypeId(Integer referenceTypeId) { this.referenceTypeId = referenceTypeId; }

    public Long getReferenceId() { return referenceId; }
    public void setReferenceId(Long referenceId) { this.referenceId = referenceId; }

    public String getEventDetails() { return eventDetails; }
    public void setEventDetails(String eventDetails) { this.eventDetails = eventDetails; }
}