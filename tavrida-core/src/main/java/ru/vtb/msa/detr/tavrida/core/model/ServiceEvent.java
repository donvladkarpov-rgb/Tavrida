package ru.vtb.msa.detr.tavrida.core.model;

import com.fasterxml.jackson.databind.JsonNode;
import jakarta.persistence.*;
import java.time.Instant;

@Entity
@Table(name = "service_events")
public class ServiceEvent {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "service_event_id")
    private Long serviceEventId;

    @Column(name = "event_time", nullable = false)
    private Instant eventTime;
    @Column(name = "event_local_time")
    private Instant eventLocalTime;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "service_event_type", nullable = false)
    private ServiceEventType serviceEventType;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "session_id")
    private UserSession session;

    @Column(name = "reference_type_id")
    private Integer referenceTypeId;

    @Column(name = "reference_id")
    private Long referenceId;

    @Column(name = "event_details", length = 256)
    private String eventDetails;

    @Column(name = "event_object", columnDefinition = "JSONB")
    private JsonNode eventObject;

    // --- getters / setters ---

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

    public Instant getEventLocalTime() {
        return eventLocalTime;
    }

    public void setEventLocalTime(Instant eventLocalTime) {
        this.eventLocalTime = eventLocalTime;
    }

    public ServiceEventType getServiceEventType() {
        return serviceEventType;
    }

    public void setServiceEventType(ServiceEventType serviceEventType) {
        this.serviceEventType = serviceEventType;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public UserSession getSession() {
        return session;
    }

    public void setSession(UserSession session) {
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