package ru.vtb.msa.detr.tavrida.api.model.route;

import com.fasterxml.jackson.databind.JsonNode;

import java.io.Serializable;
import java.time.Instant;
import java.util.UUID;

public class RouteDto implements Serializable {
    private Long routeId;
    private UUID routeGuid;
    private Long routeTypesId;
    private Long parentRouteId;
    private String routeName;
    private String description;
    private JsonNode routeObject;
    private Instant createdAt;
    private Instant updatedAt;

    public RouteDto() {}

    // Геттеры и сеттеры
    public Long getRouteId() {
        return routeId;
    }

    public void setRouteId(Long routeId) {
        this.routeId = routeId;
    }

    public UUID getRouteGuid() {
        return routeGuid;
    }

    public void setRouteGuid(UUID routeGuid) {
        this.routeGuid = routeGuid;
    }

    public Long getRouteTypesId() {
        return routeTypesId;
    }

    public void setRouteTypesId(Long routeTypesId) {
        this.routeTypesId = routeTypesId;
    }

    public Long getParentRouteId() {
        return parentRouteId;
    }

    public void setParentRouteId(Long parentRouteId) {
        this.parentRouteId = parentRouteId;
    }

    public String getRouteName() {
        return routeName;
    }

    public void setRouteName(String routeName) {
        this.routeName = routeName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public JsonNode getRouteObject() {
        return routeObject;
    }

    public void setRouteObject(JsonNode routeObject) {
        this.routeObject = routeObject;
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