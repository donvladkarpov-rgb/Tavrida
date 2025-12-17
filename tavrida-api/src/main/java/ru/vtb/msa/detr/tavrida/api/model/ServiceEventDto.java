package ru.vtb.msa.detr.tavrida.api.model;

import com.fasterxml.jackson.databind.JsonNode;
import java.io.Serializable;
import java.time.Instant;

public class ServiceEventDto implements Serializable {
    private Long serviceEventId;
    private Instant eventTime;

    // Вместо String serviceEventType — полный объект
    private ServiceEventTypeDto serviceEventType;

    // Вместо Long userId — полный объект UserDto
    private UserDto user;

    // Вместо UUID sessionId — полный объект UserSessionDto
    private UserSessionDto session;

    private Integer referenceTypeId;
    private Long referenceId;
    private String eventDetails;
    private JsonNode eventObject;

    // Constructors
    public ServiceEventDto() {}

    // Getters and Setters
    public Long getServiceEventId() {
        return serviceEventId;
    }

    public void setServiceEventId(Long serviceEventId) {
        this.serviceEventId = serviceEventId;
    }

    public Instant getEventTime() {
        return eventTime;
    }

    public void setEventTime(Instant eventTime) {
        this.eventTime = eventTime;
    }

    public ServiceEventTypeDto getServiceEventType() {
        return serviceEventType;
    }

    public void setServiceEventType(ServiceEventTypeDto serviceEventType) {
        this.serviceEventType = serviceEventType;
    }

    public UserDto getUser() {
        return user;
    }

    public void setUser(UserDto user) {
        this.user = user;
    }

    public UserSessionDto getSession() {
        return session;
    }

    public void setSession(UserSessionDto session) {
        this.session = session;
    }

    public Integer getReferenceTypeId() {
        return referenceTypeId;
    }

    public void setReferenceTypeId(Integer referenceTypeId) {
        this.referenceTypeId = referenceTypeId;
    }

    public Long getReferenceId() {
        return referenceId;
    }

    public void setReferenceId(Long referenceId) {
        this.referenceId = referenceId;
    }

    public String getEventDetails() {
        return eventDetails;
    }

    public void setEventDetails(String eventDetails) {
        this.eventDetails = eventDetails;
    }

    public JsonNode getEventObject() {
        return eventObject;
    }

    public void setEventObject(JsonNode eventObject) {
        this.eventObject = eventObject;
    }
}