package ru.vtb.msa.detr.tavrida.core.model.mapper;

import ru.vtb.msa.detr.tavrida.api.model.stop.TransportStopDto;
import ru.vtb.msa.detr.tavrida.core.model.TransportStop;

public class TransportStopMapper {

    public static TransportStopDto toDto(TransportStop entity) {
        if (entity == null) {
            return null;
        }
        TransportStopDto dto = new TransportStopDto();
        dto.setStopId(entity.getStopId());
        dto.setFareZoneId(entity.getFareZone() != null ? entity.getFareZone().getZoneId() : null);
        dto.setStopCode(entity.getStopCode());
        dto.setStopName(entity.getStopName());
        dto.setStopAddress(entity.getStopAddress());
        dto.setGeoLocation(entity.getGeoLocation());
        dto.setDescription(entity.getDescription());
        dto.setIsActive(entity.getActive());
        dto.setCreatedAt(entity.getCreatedAt());
        dto.setUpdatedAt(entity.getUpdatedAt());
        return dto;
    }

    public static TransportStop toEntity(TransportStopDto dto) {
        if (dto == null) {
            return null;
        }
        TransportStop entity = new TransportStop();
        entity.setStopId(dto.getStopId());
        entity.setStopCode(dto.getStopCode());
        entity.setStopName(dto.getStopName());
        entity.setStopAddress(dto.getStopAddress());
        entity.setGeoLocation(dto.getGeoLocation());
        entity.setDescription(dto.getDescription());
        entity.setActive(dto.getIsActive());
        entity.setCreatedAt(dto.getCreatedAt());
        entity.setUpdatedAt(dto.getUpdatedAt());
        // fareZone устанавливается отдельно в сервисе
        return entity;
    }
}