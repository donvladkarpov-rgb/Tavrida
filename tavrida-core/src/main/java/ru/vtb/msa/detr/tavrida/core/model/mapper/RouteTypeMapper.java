package ru.vtb.msa.detr.tavrida.core.model.mapper;

import ru.vtb.msa.detr.tavrida.api.model.route.RouteTypeDto;
import ru.vtb.msa.detr.tavrida.core.model.RouteType;

public class RouteTypeMapper {

    public static RouteTypeDto toDto(RouteType entity) {
        if (entity == null) {
            return null;
        }
        return new RouteTypeDto(
                entity.getRouteTypesId(),
                entity.getRouteTypesName()
        );
    }

    public static RouteType toEntity(RouteTypeDto dto) {
        if (dto == null) {
            return null;
        }
        RouteType entity = new RouteType();
        entity.setRouteTypesId(dto.getRouteTypesId());
        entity.setRouteTypesName(dto.getRouteTypesName());
        return entity;
    }
}