package ru.vtb.msa.detr.tavrida.core.service;

import ru.vtb.msa.detr.tavrida.api.model.stop.TransportStopDto;

import java.util.List;

public interface TransportStopService {
    TransportStopDto findById(Long id);
    TransportStopDto findByStopCode(String stopCode);
    List<TransportStopDto> findByZoneId(Long zoneId);
    List<TransportStopDto> findActiveStops();
    List<TransportStopDto> findAll();
    TransportStopDto create(TransportStopDto dto);
    TransportStopDto update(Long id, TransportStopDto dto);
    void delete(Long id);
}