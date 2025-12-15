package ru.vtb.msa.detr.tavrida.core.model.mapper;

import ru.vtb.msa.detr.tavrida.api.model.route.RouteDto;
import ru.vtb.msa.detr.tavrida.core.model.Route;

public class RouteMapper {

    public static RouteDto toDto(Route entity) {
        if (entity == null) {
            return null;
        }
        RouteDto dto = new RouteDto();
        dto.setRouteId(entity.getRouteId());
        dto.setRouteGuid(entity.getRouteGuid());
        dto.setRouteTypesId(entity.getRouteType() != null ? entity.getRouteType().getRouteTypesId() : null);
        dto.setParentRouteId(entity.getParentRoute() != null ? entity.getParentRoute().getRouteId() : null);
        dto.setRouteName(entity.getRouteName());
        dto.setDescription(entity.getDescription());
        dto.setRouteObject(entity.getRouteObject());
        dto.setCreatedAt(entity.getCreatedAt());
        dto.setUpdatedAt(entity.getUpdatedAt());
        return dto;
    }

    public static Route toEntity(RouteDto dto) {
        if (dto == null) {
            return null;
        }
        Route entity = new Route();
        entity.setRouteId(dto.getRouteId());
        entity.setRouteGuid(dto.getRouteGuid());
        entity.setRouteName(dto.getRouteName());
        entity.setDescription(dto.getDescription());
        entity.setRouteObject(dto.getRouteObject());
        entity.setCreatedAt(dto.getCreatedAt());
        entity.setUpdatedAt(dto.getUpdatedAt());
        // routeType и parentRoute устанавливаются отдельно в сервисе
        return entity;
    }
}