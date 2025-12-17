package ru.vtb.msa.detr.tavrida.core.model;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import jakarta.persistence.*;
import ru.vtb.msa.detr.tavrida.core.util.JsonUtils;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "routes")
public class Route {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "route_id")
    private Long routeId;

    @Column(name = "route_guid", nullable = false, unique = true)
    private UUID routeGuid;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "route_types_id", nullable = false)
    private RouteType routeType;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_route_id")
    private Route parentRoute; // самосвязь

    @Column(name = "route_name", nullable = false, length = 128)
    private String routeName;

    @Column(name = "description", length = 512)
    private String description;

    @Column(name = "route_object", columnDefinition = "JSONB")
    private String routeObject;

    @Column(name = "created_at", nullable = false)
    private Instant createdAt;

    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt;

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

    public RouteType getRouteType() {
        return routeType;
    }

    public void setRouteType(RouteType routeType) {
        this.routeType = routeType;
    }

    public Route getParentRoute() {
        return parentRoute;
    }

    public void setParentRoute(Route parentRoute) {
        this.parentRoute = parentRoute;
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

    public JsonNode getRouteObject()  {
        try {
            return routeObject == null ? null : JsonUtils.readTree(routeObject);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    // сеттер
    public void setRouteObject(JsonNode node)  {
        try {
            this.routeObject = node == null ? null : JsonUtils.writeValue(node);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
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