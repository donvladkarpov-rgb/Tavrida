package ru.vtb.msa.detr.tavrida.core.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "route_types")
public class RouteType {
    @Id
    @Column(name = "route_types_id", nullable = false)
    private Long routeTypesId;

    @Column(name = "route_types_name", nullable = false, length = 16)
    private String routeTypesName;

    // геттеры/сеттеры

    public Long getRouteTypesId() {
        return routeTypesId;
    }

    public void setRouteTypesId(Long routeTypesId) {
        this.routeTypesId = routeTypesId;
    }

    public String getRouteTypesName() {
        return routeTypesName;
    }

    public void setRouteTypesName(String routeTypesName) {
        this.routeTypesName = routeTypesName;
    }
}