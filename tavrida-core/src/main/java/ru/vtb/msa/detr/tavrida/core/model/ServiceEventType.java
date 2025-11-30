package ru.vtb.msa.detr.tavrida.core.model;

import jakarta.persistence.*;
import org.hibernate.annotations.JdbcType;
import org.hibernate.type.descriptor.jdbc.CharJdbcType;

@Entity
@Table(name = "service_event_types")
public class ServiceEventType {

    @Id
    @Column(name = "event_type", length = 2)
    @JdbcType(CharJdbcType.class)
    private String eventType;

    @Column(name = "event_type_name", length = 128, nullable = false)
    private String eventTypeName;

    // Constructors
    public ServiceEventType() {}

    public ServiceEventType(String eventType, String eventTypeName) {
        this.eventType = eventType;
        this.eventTypeName = eventTypeName;
    }

    // Getters and Setters
    public String getEventType() { return eventType; }
    public void setEventType(String eventType) { this.eventType = eventType; }

    public String getEventTypeName() { return eventTypeName; }
    public void setEventTypeName(String eventTypeName) { this.eventTypeName = eventTypeName; }
}