package ru.vtb.msa.detr.tavrida.core.service;

import ru.vtb.msa.detr.tavrida.api.model.trip.TripDto;

import java.util.List;
import java.util.UUID;

public interface TripService {
    TripDto findById(Long id);
    List<TripDto> findByRouteId(Long routeId);
    List<TripDto> findBySessionId(UUID sessionId);
    List<TripDto> findAll();
    TripDto create(TripDto dto);
    TripDto update(Long id, TripDto dto);
    void delete(Long id);
}