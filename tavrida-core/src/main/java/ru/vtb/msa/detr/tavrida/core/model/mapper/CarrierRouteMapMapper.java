package ru.vtb.msa.detr.tavrida.core.model.mapper;

import ru.vtb.msa.detr.tavrida.api.model.carrier.CarrierRouteMapDto;
import ru.vtb.msa.detr.tavrida.core.model.CarrierRouteMap;

public class CarrierRouteMapMapper {

    public static CarrierRouteMapDto toDto(CarrierRouteMap entity) {
        if (entity == null) {
            return null;
        }
        CarrierRouteMapDto dto = new CarrierRouteMapDto();
        dto.setCarrierRouteMapId(entity.getCarrierRouteMapId());
        dto.setCarrierId(entity.getCarrier() != null ? entity.getCarrier().getCarrierId() : null);
        dto.setRouteId(entity.getRoute() != null ? entity.getRoute().getRouteId() : null);
        dto.setCreatedAt(entity.getCreatedAt());
        dto.setUpdatedAt(entity.getUpdatedAt());
        return dto;
    }

    public static CarrierRouteMap toEntity(CarrierRouteMapDto dto) {
        if (dto == null) {
            return null;
        }
        CarrierRouteMap entity = new CarrierRouteMap();
        entity.setCarrierRouteMapId(dto.getCarrierRouteMapId());
        entity.setCreatedAt(dto.getCreatedAt());
        entity.setUpdatedAt(dto.getUpdatedAt());
        // carrier и route устанавливаются отдельно в сервисе
        return entity;
    }
}