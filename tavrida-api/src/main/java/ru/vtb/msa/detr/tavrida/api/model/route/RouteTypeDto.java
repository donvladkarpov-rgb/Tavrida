package ru.vtb.msa.detr.tavrida.api.model.route;

import java.io.Serializable;

public class RouteTypeDto implements Serializable {
    private Long routeTypesId;
    private String routeTypesName;

    public RouteTypeDto() {}

    public RouteTypeDto(Long routeTypesId, String routeTypesName) {
        this.routeTypesId = routeTypesId;
        this.routeTypesName = routeTypesName;
    }

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