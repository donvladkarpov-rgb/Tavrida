package ru.vtb.msa.detr.tavrida.core.service;

import ru.vtb.msa.detr.tavrida.api.model.route.RouteDto;

import java.util.List;
import java.util.UUID;

public interface RouteService {
    RouteDto findById(Long id);
    RouteDto findByRouteGuid(UUID routeGuid);
    List<RouteDto> findRootRoutes();
    List<RouteDto> findChildren(Long parentId);
    List<RouteDto> findAll();
    RouteDto create(RouteDto dto);
    RouteDto update(Long id, RouteDto dto);
    void delete(Long id);
}