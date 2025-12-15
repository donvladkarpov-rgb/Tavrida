package ru.vtb.msa.detr.tavrida.core.service;

import ru.vtb.msa.detr.tavrida.api.model.stop.StopsRouteMapDto;

import java.util.List;

public interface StopsRouteMapService {
    StopsRouteMapDto findById(Long id);
    List<StopsRouteMapDto> findByRouteId(Long routeId);
    List<StopsRouteMapDto> findByStopId(Long stopId);
    List<StopsRouteMapDto> findAll();
    StopsRouteMapDto create(StopsRouteMapDto dto);
    StopsRouteMapDto update(Long id, StopsRouteMapDto dto);
    void delete(Long id);
}