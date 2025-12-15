package ru.vtb.msa.detr.tavrida.core.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.vtb.msa.detr.tavrida.core.exception.EntityNotFoundException;
import ru.vtb.msa.detr.tavrida.core.model.mapper.RouteMapper;
import ru.vtb.msa.detr.tavrida.core.model.Route;
import ru.vtb.msa.detr.tavrida.core.model.RouteType;
import ru.vtb.msa.detr.tavrida.core.repo.RouteRepository;
import ru.vtb.msa.detr.tavrida.core.repo.RouteTypeRepository;
import ru.vtb.msa.detr.tavrida.api.model.route.RouteDto;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class RouteServiceImpl implements RouteService {

    private final RouteRepository routeRepository;
    private final RouteTypeRepository routeTypeRepository;

    public RouteServiceImpl(RouteRepository routeRepository, RouteTypeRepository routeTypeRepository) {
        this.routeRepository = routeRepository;
        this.routeTypeRepository = routeTypeRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public RouteDto findById(Long id) {
        return routeRepository.findById(id)
                .map(RouteMapper::toDto)
                .orElseThrow(() -> new EntityNotFoundException("Route", "id", id));
    }

    @Override
    @Transactional(readOnly = true)
    public RouteDto findByRouteGuid(UUID routeGuid) {
        return routeRepository.findByRouteGuid(routeGuid)
                .map(RouteMapper::toDto)
                .orElseThrow(() -> new EntityNotFoundException("Route", "routeGuid", routeGuid.toString()));
    }

    @Override
    @Transactional(readOnly = true)
    public List<RouteDto> findRootRoutes() {
        return routeRepository.findByParentRouteIsNull().stream()
                .map(RouteMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<RouteDto> findChildren(Long parentId) {
        return routeRepository.findByParentRoute_RouteId(parentId).stream()
                .map(RouteMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<RouteDto> findAll() {
        return routeRepository.findAll().stream()
                .map(RouteMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public RouteDto create(RouteDto dto) {
        Route entity = RouteMapper.toEntity(dto);

        // Устанавливаем тип маршрута
        if (dto.getRouteTypesId() != null) {
            RouteType routeType = routeTypeRepository.findById(dto.getRouteTypesId())
                    .orElseThrow(() -> new EntityNotFoundException("RouteType", "id", dto.getRouteTypesId()));
            entity.setRouteType(routeType);
        }

        // Устанавливаем родительский маршрут
        if (dto.getParentRouteId() != null) {
            Route parent = routeRepository.findById(dto.getParentRouteId())
                    .orElseThrow(() -> new EntityNotFoundException("Route", "id", dto.getParentRouteId()));
            entity.setParentRoute(parent);
        }

        Route saved = routeRepository.save(entity);
        return RouteMapper.toDto(saved);
    }

    @Override
    @Transactional
    public RouteDto update(Long id, RouteDto dto) {
        Route entity = routeRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Route", "id", id));

        entity.setRouteGuid(dto.getRouteGuid());
        entity.setRouteName(dto.getRouteName());
        entity.setDescription(dto.getDescription());
        entity.setRouteObject(dto.getRouteObject());
        entity.setUpdatedAt(dto.getUpdatedAt());

        // Обновляем тип маршрута
        if (dto.getRouteTypesId() != null) {
            RouteType routeType = routeTypeRepository.findById(dto.getRouteTypesId())
                    .orElseThrow(() -> new EntityNotFoundException("RouteType", "id", dto.getRouteTypesId()));
            entity.setRouteType(routeType);
        } else {
            entity.setRouteType(null);
        }

        // Обновляем родительский маршрут
        if (dto.getParentRouteId() != null) {
            Route parent = routeRepository.findById(dto.getParentRouteId())
                    .orElseThrow(() -> new EntityNotFoundException("Route", "id", dto.getParentRouteId()));
            entity.setParentRoute(parent);
        } else {
            entity.setParentRoute(null);
        }

        Route updated = routeRepository.save(entity);
        return RouteMapper.toDto(updated);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        if (!routeRepository.existsById(id)) {
            throw new EntityNotFoundException("Route", "id", id);
        }
        routeRepository.deleteById(id);
    }
}