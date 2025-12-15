package ru.vtb.msa.detr.tavrida.core.service;

import ru.vtb.msa.detr.tavrida.api.model.route.RouteTypeDto;

import java.util.List;

public interface RouteTypeService {
    RouteTypeDto findById(Long id);
    RouteTypeDto findByName(String name);
    List<RouteTypeDto> findAll();
    RouteTypeDto create(RouteTypeDto dto);
    RouteTypeDto update(Long id, RouteTypeDto dto);
    void delete(Long id);
}