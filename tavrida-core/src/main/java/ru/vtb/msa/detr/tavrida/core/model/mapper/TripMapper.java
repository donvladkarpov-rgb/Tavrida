package ru.vtb.msa.detr.tavrida.core.model.mapper;

import ru.vtb.msa.detr.tavrida.api.model.trip.TripDto;
import ru.vtb.msa.detr.tavrida.core.model.Trip;

public class TripMapper {

    public static TripDto toDto(Trip entity) {
        if (entity == null) {
            return null;
        }
        TripDto dto = new TripDto();
        dto.setTripId(entity.getTripId());
        dto.setRouteId(entity.getRoute() != null ? entity.getRoute().getRouteId() : null);
        dto.setSessionId(entity.getSession() != null ? entity.getSession().getSessionId() : null);
        dto.setStartedAt(entity.getStartedAt());
        dto.setClosedAt(entity.getClosedAt());
        dto.setStartedAtLocal(entity.getStartedAtLocal());
        dto.setClosedAtLocal(entity.getClosedAtLocal());
        return dto;
    }

    public static Trip toEntity(TripDto dto) {
        if (dto == null) {
            return null;
        }
        Trip entity = new Trip();
        entity.setTripId(dto.getTripId());
        entity.setStartedAt(dto.getStartedAt());
        entity.setClosedAt(dto.getClosedAt());
        entity.setStartedAtLocal(dto.getStartedAtLocal());
        entity.setClosedAtLocal(dto.getClosedAtLocal());
        // route и session устанавливаются отдельно в сервисе
        return entity;
    }
}