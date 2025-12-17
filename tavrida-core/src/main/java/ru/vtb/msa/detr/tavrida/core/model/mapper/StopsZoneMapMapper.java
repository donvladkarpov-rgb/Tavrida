package ru.vtb.msa.detr.tavrida.core.model.mapper;

import ru.vtb.msa.detr.tavrida.api.model.carrier.FareZoneDto;
import ru.vtb.msa.detr.tavrida.api.model.stop.StopsZoneMapDto;
import ru.vtb.msa.detr.tavrida.api.model.stop.TransportStopDto;
import ru.vtb.msa.detr.tavrida.core.model.FareZone;
import ru.vtb.msa.detr.tavrida.core.model.StopsZoneMap;
import ru.vtb.msa.detr.tavrida.core.model.TransportStop;

public class StopsZoneMapMapper {

    // Сущность → DTO
    public static StopsZoneMapDto toDto(StopsZoneMap entity) {
        if (entity == null) return null;

        StopsZoneMapDto dto = new StopsZoneMapDto();
        dto.setId(entity.getId());
        dto.setStop(toTransportStopDto(entity.getStop()));
        dto.setZone(toFareZoneDto(entity.getZone()));
        dto.setCreatedAt(entity.getCreatedAt());
        dto.setUpdatedAt(entity.getUpdatedAt());
        return dto;
    }

    // DTO → Сущность (только для создания, связи по ID)
    public static StopsZoneMap toEntity(StopsZoneMapDto dto) {
        if (dto == null || dto.getStop() == null || dto.getZone() == null) {
            return null;
        }

        TransportStop stop = new TransportStop();
        stop.setStopId(dto.getStop().getStopId());

        FareZone zone = new FareZone();
        zone.setZoneId(dto.getZone().getZoneId());

        return new StopsZoneMap(stop, zone);
    }

    // Вспомогательные методы
    private static TransportStopDto toTransportStopDto(TransportStop stop) {
        if (stop == null) return null;
        TransportStopDto dto = new TransportStopDto();
        dto.setStopId(stop.getStopId());
        dto.setFareZoneId(stop.getFareZone() != null ? stop.getFareZone().getZoneId() : null);
        dto.setStopCode(stop.getStopCode());
        dto.setStopName(stop.getStopName());
        dto.setStopAddress(stop.getStopAddress());
        dto.setGeoLocation(stop.getGeoLocation());
        dto.setDescription(stop.getDescription());
        dto.setIsActive(stop.getActive());
        dto.setCreatedAt(stop.getCreatedAt());
        dto.setUpdatedAt(stop.getUpdatedAt());
        return dto;
    }

    private static FareZoneDto toFareZoneDto(FareZone zone) {
        if (zone == null) return null;
        FareZoneDto dto = new FareZoneDto();
        dto.setZoneId(zone.getZoneId());
        dto.setZoneCode(zone.getZoneCode());
        dto.setZoneName(zone.getZoneName());
        dto.setDescription(zone.getDescription());
        dto.setCreatedAt(zone.getCreatedAt());
        dto.setUpdatedAt(zone.getUpdatedAt());
        return dto;
    }
}