package ru.vtb.msa.detr.tavrida.core.service;

import ru.vtb.msa.detr.tavrida.api.model.carrier.CarrierZoneMapDto;

import java.util.List;

public interface CarrierZoneMapService {
    CarrierZoneMapDto findById(Long id);
    CarrierZoneMapDto findByCarrierIdAndZoneId(Long carrierId, Long zoneId);
    List<CarrierZoneMapDto> findByCarrierId(Long carrierId);
    List<CarrierZoneMapDto> findByZoneId(Long zoneId);
    List<CarrierZoneMapDto> findAll();
    CarrierZoneMapDto create(CarrierZoneMapDto dto);
    CarrierZoneMapDto update(Long id, CarrierZoneMapDto dto);
    void delete(Long id);
}