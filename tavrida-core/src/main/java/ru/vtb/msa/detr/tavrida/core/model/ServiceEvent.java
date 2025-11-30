package ru.vtb.msa.detr.tavrida.core.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "service_events")
public class ServiceEvent {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "service_event_id")
    private Long serviceEventId;

    @Column(name = "event_time", nullable = false)
    private LocalDateTime eventTime;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "service_event_type", nullable = false)
    private ServiceEventType serviceEventType;

    @Column(name = "doer_user_id")
    private Long doerUserId;

    @Column(name = "reference_type_id")
    private Integer referenceTypeId;

    @Column(name = "reference_id")
    private Long referenceId;

    @Column(name = "event_details", length = 256)
    private String eventDetails;

    // Constructors
    public ServiceEvent() {}

    // Getters and Setters
    public Long getServiceEventId() { return serviceEventId; }
    public void setServiceEventId(Long serviceEventId) { this.serviceEventId = serviceEventId; }

    public LocalDateTime getEventTime() { return eventTime; }
    public void setEventTime(LocalDateTime eventTime) { this.eventTime = eventTime; }

    public ServiceEventType getServiceEventType() { return serviceEventType; }
    public void setServiceEventType(ServiceEventType serviceEventType) { this.serviceEventType = serviceEventType; }

    public Long getDoerUserId() { return doerUserId; }
    public void setDoerUserId(Long doerUserId) { this.doerUserId = doerUserId; }

    public Integer getReferenceTypeId() { return referenceTypeId; }
    public void setReferenceTypeId(Integer referenceTypeId) { this.referenceTypeId = referenceTypeId; }

    public Long getReferenceId() { return referenceId; }
    public void setReferenceId(Long referenceId) { this.referenceId = referenceId; }

    public String getEventDetails() { return eventDetails; }
    public void setEventDetails(String eventDetails) { this.eventDetails = eventDetails; }
}