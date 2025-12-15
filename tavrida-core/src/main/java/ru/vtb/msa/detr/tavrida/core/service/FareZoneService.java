package ru.vtb.msa.detr.tavrida.core.service;

import ru.vtb.msa.detr.tavrida.api.model.carrier.FareZoneDto;

import java.util.List;

public interface FareZoneService {
    FareZoneDto findById(Long id);
    FareZoneDto findByZoneCode(String zoneCode);
    FareZoneDto findByZoneName(String zoneName);
    List<FareZoneDto> findAll();
    FareZoneDto create(FareZoneDto dto);
    FareZoneDto update(Long id, FareZoneDto dto);
    void delete(Long id);
}