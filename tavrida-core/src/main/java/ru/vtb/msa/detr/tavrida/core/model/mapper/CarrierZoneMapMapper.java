package ru.vtb.msa.detr.tavrida.core.model.mapper;

import ru.vtb.msa.detr.tavrida.api.model.carrier.CarrierZoneMapDto;
import ru.vtb.msa.detr.tavrida.core.model.CarrierZoneMap;

public class CarrierZoneMapMapper {

    public static CarrierZoneMapDto toDto(CarrierZoneMap entity) {
        if (entity == null) {
            return null;
        }
        CarrierZoneMapDto dto = new CarrierZoneMapDto();
        dto.setCarrierZoneMapId(entity.getCarrierZoneMapId());
        dto.setCarrierId(entity.getCarrier() != null ? entity.getCarrier().getCarrierId() : null);
        dto.setZoneId(entity.getFareZone() != null ? entity.getFareZone().getZoneId() : null);
        dto.setBaseFare(entity.getBaseFare());
        dto.setCreatedAt(entity.getCreatedAt());
        dto.setUpdatedAt(entity.getUpdatedAt());
        return dto;
    }

    public static CarrierZoneMap toEntity(CarrierZoneMapDto dto) {
        if (dto == null) {
            return null;
        }
        CarrierZoneMap entity = new CarrierZoneMap();
        entity.setCarrierZoneMapId(dto.getCarrierZoneMapId());
        entity.setBaseFare(dto.getBaseFare());
        entity.setCreatedAt(dto.getCreatedAt());
        entity.setUpdatedAt(dto.getUpdatedAt());
        // carrier и fareZone устанавливаются отдельно в сервисе
        return entity;
    }
}