package ru.vtb.msa.detr.tavrida.core.model.mapper;

import ru.vtb.msa.detr.tavrida.api.model.carrier.FareZoneDto;
import ru.vtb.msa.detr.tavrida.core.model.FareZone;

public class FareZoneMapper {

    public static FareZoneDto toDto(FareZone entity) {
        if (entity == null) {
            return null;
        }
        FareZoneDto dto = new FareZoneDto();
        dto.setZoneId(entity.getZoneId());
        dto.setZoneCode(entity.getZoneCode());
        dto.setZoneName(entity.getZoneName());
        dto.setDescription(entity.getDescription());
        dto.setCreatedAt(entity.getCreatedAt());
        dto.setUpdatedAt(entity.getUpdatedAt());
        return dto;
    }

    public static FareZone toEntity(FareZoneDto dto) {
        if (dto == null) {
            return null;
        }
        FareZone entity = new FareZone();
        entity.setZoneId(dto.getZoneId());
        entity.setZoneCode(dto.getZoneCode());
        entity.setZoneName(dto.getZoneName());
        entity.setDescription(dto.getDescription());
        entity.setCreatedAt(dto.getCreatedAt());
        entity.setUpdatedAt(dto.getUpdatedAt());
        return entity;
    }
}