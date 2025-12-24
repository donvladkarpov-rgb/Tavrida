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
        dto.setStartedAtServer(entity.getStartedAt());
        dto.setClosedAtServer(entity.getClosedAt());
        dto.setStartedAtTerminal(entity.getStartedAtLocal());
        dto.setClosedAtTerminal(entity.getClosedAtLocal());
        return dto;
    }

    public static Trip toEntity(TripDto dto) {
        if (dto == null) {
            return null;
        }
        Trip entity = new Trip();
        entity.setTripId(dto.getTripId());
        entity.setStartedAt(dto.getStartedAtServer());
        entity.setClosedAt(dto.getClosedAtServer());
        entity.setStartedAtLocal(dto.getStartedAtTerminal());
        entity.setClosedAtLocal(dto.getClosedAtTerminal());
        // route и session устанавливаются отдельно в сервисе
        return entity;
    }
}