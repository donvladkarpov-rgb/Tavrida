package ru.vtb.msa.detr.tavrida.core.service;

import ru.vtb.msa.detr.tavrida.api.model.carrier.CarrierRouteMapDto;

import java.util.List;

public interface CarrierRouteMapService {
    CarrierRouteMapDto findById(Long id);
    List<CarrierRouteMapDto> findByCarrierId(Long carrierId);
    List<CarrierRouteMapDto> findByRouteId(Long routeId);
    List<CarrierRouteMapDto> findAll();
    CarrierRouteMapDto create(CarrierRouteMapDto dto);
    CarrierRouteMapDto update(Long id, CarrierRouteMapDto dto);
    void delete(Long id);
}