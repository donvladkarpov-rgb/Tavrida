package ru.vtb.msa.detr.tavrida.core.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.vtb.msa.detr.tavrida.core.exception.EntityNotFoundException;
import ru.vtb.msa.detr.tavrida.core.model.mapper.StopsRouteMapMapper;
import ru.vtb.msa.detr.tavrida.core.model.Route;
import ru.vtb.msa.detr.tavrida.core.model.StopsRouteMap;
import ru.vtb.msa.detr.tavrida.core.model.TransportStop;
import ru.vtb.msa.detr.tavrida.core.repo.RouteRepository;
import ru.vtb.msa.detr.tavrida.core.repo.StopsRouteMapRepository;
import ru.vtb.msa.detr.tavrida.core.repo.TransportStopRepository;
import ru.vtb.msa.detr.tavrida.api.model.stop.StopsRouteMapDto;

import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class StopsRouteMapServiceImpl implements StopsRouteMapService {

    private final StopsRouteMapRepository repository;
    private final TransportStopRepository stopRepository;
    private final RouteRepository routeRepository;

    public StopsRouteMapServiceImpl(
            StopsRouteMapRepository repository,
            TransportStopRepository stopRepository,
            RouteRepository routeRepository
    ) {
        this.repository = repository;
        this.stopRepository = stopRepository;
        this.routeRepository = routeRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public StopsRouteMapDto findById(Long id) {
        return repository.findById(id)
                .map(StopsRouteMapMapper::toDto)
                .orElseThrow(() -> new EntityNotFoundException("StopsRouteMap", "id", id));
    }

    @Override
    @Transactional(readOnly = true)
    public List<StopsRouteMapDto> findByRouteId(Long routeId) {
        return repository.findByRoute_RouteIdOrderBySerialNumberAsc(routeId).stream()
                .map(StopsRouteMapMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<StopsRouteMapDto> findByStopId(Long stopId) {
        return repository.findByStop_StopId(stopId).stream()
                .map(StopsRouteMapMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<StopsRouteMapDto> findAll() {
        return repository.findAll().stream()
                .map(StopsRouteMapMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public StopsRouteMapDto create(StopsRouteMapDto dto) {
        StopsRouteMap entity = StopsRouteMapMapper.toEntity(dto);

        // Устанавливаем остановку
        if (dto.getStopId() != null) {
            TransportStop stop = stopRepository.findById(dto.getStopId())
                    .orElseThrow(() -> new EntityNotFoundException("TransportStop", "id", dto.getStopId()));
            entity.setStop(stop);
        }

        // Устанавливаем маршрут
        if (dto.getRouteId() != null) {
            Route route = routeRepository.findById(dto.getRouteId())
                    .orElseThrow(() -> new EntityNotFoundException("Route", "id", dto.getRouteId()));
            entity.setRoute(route);
        }

        entity.setCreatedAt(Instant.now());
        entity.setUpdatedAt(Instant.now());

        StopsRouteMap saved = repository.save(entity);
        return StopsRouteMapMapper.toDto(saved);
    }

    @Override
    @Transactional
    public StopsRouteMapDto update(Long id, StopsRouteMapDto dto) {
        StopsRouteMap entity = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("StopsRouteMap", "id", id));

        entity.setSerialNumber(dto.getSerialNumber());
        entity.setUpdatedAt(Instant.now());

        // Обновляем остановку
        if (dto.getStopId() != null) {
            TransportStop stop = stopRepository.findById(dto.getStopId())
                    .orElseThrow(() -> new EntityNotFoundException("TransportStop", "id", dto.getStopId()));
            entity.setStop(stop);
        } else {
            entity.setStop(null);
        }

        // Обновляем маршрут
        if (dto.getRouteId() != null) {
            Route route = routeRepository.findById(dto.getRouteId())
                    .orElseThrow(() -> new EntityNotFoundException("Route", "id", dto.getRouteId()));
            entity.setRoute(route);
        } else {
            entity.setRoute(null);
        }

        StopsRouteMap updated = repository.save(entity);
        return StopsRouteMapMapper.toDto(updated);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        if (!repository.existsById(id)) {
            throw new EntityNotFoundException("StopsRouteMap", "id", id);
        }
        repository.deleteById(id);
    }
}