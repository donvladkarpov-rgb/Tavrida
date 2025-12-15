package ru.vtb.msa.detr.tavrida.core.model.mapper;

import ru.vtb.msa.detr.tavrida.api.model.stop.StopsRouteMapDto;
import ru.vtb.msa.detr.tavrida.core.model.StopsRouteMap;

public class StopsRouteMapMapper {

    public static StopsRouteMapDto toDto(StopsRouteMap entity) {
        if (entity == null) {
            return null;
        }
        StopsRouteMapDto dto = new StopsRouteMapDto();
        dto.setStopsRouteMapId(entity.getStopsRouteMapId());
        dto.setStopId(entity.getStop() != null ? entity.getStop().getStopId() : null);
        dto.setRouteId(entity.getRoute() != null ? entity.getRoute().getRouteId() : null);
        dto.setSerialNumber(entity.getSerialNumber());
        dto.setCreatedAt(entity.getCreatedAt());
        dto.setUpdatedAt(entity.getUpdatedAt());
        return dto;
    }

    public static StopsRouteMap toEntity(StopsRouteMapDto dto) {
        if (dto == null) {
            return null;
        }
        StopsRouteMap entity = new StopsRouteMap();
        entity.setStopsRouteMapId(dto.getStopsRouteMapId());
        entity.setSerialNumber(dto.getSerialNumber());
        entity.setCreatedAt(dto.getCreatedAt());
        entity.setUpdatedAt(dto.getUpdatedAt());
        // stop и route устанавливаются отдельно в сервисе
        return entity;
    }
}