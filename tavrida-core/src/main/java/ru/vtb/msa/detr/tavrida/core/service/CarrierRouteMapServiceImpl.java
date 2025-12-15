package ru.vtb.msa.detr.tavrida.core.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.vtb.msa.detr.tavrida.core.exception.EntityNotFoundException;
import ru.vtb.msa.detr.tavrida.core.model.mapper.CarrierRouteMapMapper;
import ru.vtb.msa.detr.tavrida.core.model.Carrier;
import ru.vtb.msa.detr.tavrida.core.model.CarrierRouteMap;
import ru.vtb.msa.detr.tavrida.core.model.Route;
import ru.vtb.msa.detr.tavrida.core.repo.CarrierRepository;
import ru.vtb.msa.detr.tavrida.core.repo.CarrierRouteMapRepository;
import ru.vtb.msa.detr.tavrida.core.repo.RouteRepository;
import ru.vtb.msa.detr.tavrida.api.model.carrier.CarrierRouteMapDto;

import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CarrierRouteMapServiceImpl implements CarrierRouteMapService {

    private final CarrierRouteMapRepository repository;
    private final CarrierRepository carrierRepository;
    private final RouteRepository routeRepository;

    public CarrierRouteMapServiceImpl(
            CarrierRouteMapRepository repository,
            CarrierRepository carrierRepository,
            RouteRepository routeRepository
    ) {
        this.repository = repository;
        this.carrierRepository = carrierRepository;
        this.routeRepository = routeRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public CarrierRouteMapDto findById(Long id) {
        return repository.findById(id)
                .map(CarrierRouteMapMapper::toDto)
                .orElseThrow(() -> new EntityNotFoundException("CarrierRouteMap", "id", id));
    }

    @Override
    @Transactional(readOnly = true)
    public List<CarrierRouteMapDto> findByCarrierId(Long carrierId) {
        return repository.findByCarrier_CarrierId(carrierId).stream()
                .map(CarrierRouteMapMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<CarrierRouteMapDto> findByRouteId(Long routeId) {
        return repository.findByRoute_RouteId(routeId).stream()
                .map(CarrierRouteMapMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<CarrierRouteMapDto> findAll() {
        return repository.findAll().stream()
                .map(CarrierRouteMapMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public CarrierRouteMapDto create(CarrierRouteMapDto dto) {
        CarrierRouteMap entity = CarrierRouteMapMapper.toEntity(dto);

        // Устанавливаем перевозчика
        if (dto.getCarrierId() != null) {
            Carrier carrier = carrierRepository.findById(dto.getCarrierId())
                    .orElseThrow(() -> new EntityNotFoundException("Carrier", "id", dto.getCarrierId()));
            entity.setCarrier(carrier);
        }

        // Устанавливаем маршрут
        if (dto.getRouteId() != null) {
            Route route = routeRepository.findById(dto.getRouteId())
                    .orElseThrow(() -> new EntityNotFoundException("Route", "id", dto.getRouteId()));
            entity.setRoute(route);
        }

        entity.setCreatedAt(Instant.now());
        entity.setUpdatedAt(Instant.now());

        CarrierRouteMap saved = repository.save(entity);
        return CarrierRouteMapMapper.toDto(saved);
    }

    @Override
    @Transactional
    public CarrierRouteMapDto update(Long id, CarrierRouteMapDto dto) {
        CarrierRouteMap entity = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("CarrierRouteMap", "id", id));

        // Обновляем перевозчика
        if (dto.getCarrierId() != null) {
            Carrier carrier = carrierRepository.findById(dto.getCarrierId())
                    .orElseThrow(() -> new EntityNotFoundException("Carrier", "id", dto.getCarrierId()));
            entity.setCarrier(carrier);
        } else {
            entity.setCarrier(null);
        }

        // Обновляем маршрут
        if (dto.getRouteId() != null) {
            Route route = routeRepository.findById(dto.getRouteId())
                    .orElseThrow(() -> new EntityNotFoundException("Route", "id", dto.getRouteId()));
            entity.setRoute(route);
        } else {
            entity.setRoute(null);
        }

        entity.setUpdatedAt(Instant.now());

        CarrierRouteMap updated = repository.save(entity);
        return CarrierRouteMapMapper.toDto(updated);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        if (!repository.existsById(id)) {
            throw new EntityNotFoundException("CarrierRouteMap", "id", id);
        }
        repository.deleteById(id);
    }
}