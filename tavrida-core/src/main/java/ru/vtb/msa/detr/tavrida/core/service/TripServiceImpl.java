package ru.vtb.msa.detr.tavrida.core.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.vtb.msa.detr.tavrida.core.exception.EntityNotFoundException;
import ru.vtb.msa.detr.tavrida.core.model.mapper.TripMapper;
import ru.vtb.msa.detr.tavrida.core.model.Route;
import ru.vtb.msa.detr.tavrida.core.model.Trip;
import ru.vtb.msa.detr.tavrida.core.model.UserSession;
import ru.vtb.msa.detr.tavrida.core.repo.RouteRepository;
import ru.vtb.msa.detr.tavrida.core.repo.TripRepository;
import ru.vtb.msa.detr.tavrida.core.repo.UserSessionRepository;
import ru.vtb.msa.detr.tavrida.api.model.trip.TripDto;

import java.time.Instant;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class TripServiceImpl implements TripService {

    private final TripRepository repository;
    private final RouteRepository routeRepository;
    private final UserSessionRepository sessionRepository;

    public TripServiceImpl(
            TripRepository repository,
            RouteRepository routeRepository,
            UserSessionRepository sessionRepository
    ) {
        this.repository = repository;
        this.routeRepository = routeRepository;
        this.sessionRepository = sessionRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public TripDto findById(Long id) {
        return repository.findById(id)
                .map(TripMapper::toDto)
                .orElseThrow(() -> new EntityNotFoundException("Trip", "id", id));
    }

    @Override
    @Transactional(readOnly = true)
    public List<TripDto> findByRouteId(Long routeId) {
        return repository.findByRoute_RouteId(routeId).stream()
                .map(TripMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<TripDto> findBySessionId(UUID sessionId) {
        return repository.findBySession_SessionId(sessionId).stream()
                .map(TripMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<TripDto> findAll() {
        return repository.findAll().stream()
                .map(TripMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public TripDto create(TripDto dto) {
        Trip entity = TripMapper.toEntity(dto);

        // Устанавливаем маршрут
        if (dto.getRouteId() != null) {
            Route route = routeRepository.findById(dto.getRouteId())
                    .orElseThrow(() -> new EntityNotFoundException("Route", "id", dto.getRouteId()));
            entity.setRoute(route);
        }

        // Устанавливаем сессию
        if (dto.getSessionId() != null) {
            UserSession session = sessionRepository.findById(dto.getSessionId())
                    .orElseThrow(() -> new EntityNotFoundException("UserSession", "id", dto.getSessionId().toString()));
            entity.setSession(session);
        }

        entity.setStartedAt(dto.getStartedAt() != null ? dto.getStartedAt() : Instant.now());
        entity.setClosedAt(dto.getClosedAt() != null ? dto.getClosedAt() : Instant.now());

        Trip saved = repository.save(entity);
        return TripMapper.toDto(saved);
    }

    @Override
    @Transactional
    public TripDto update(Long id, TripDto dto) {
        Trip entity = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Trip", "id", id));

        // Обновляем маршрут
        if (dto.getRouteId() != null) {
            Route route = routeRepository.findById(dto.getRouteId())
                    .orElseThrow(() -> new EntityNotFoundException("Route", "id", dto.getRouteId()));
            entity.setRoute(route);
        } else {
            entity.setRoute(null);
        }

        // Обновляем сессию
        if (dto.getSessionId() != null) {
            UserSession session = sessionRepository.findById(dto.getSessionId())
                    .orElseThrow(() -> new EntityNotFoundException("UserSession", "id", dto.getSessionId().toString()));
            entity.setSession(session);
        } else {
            entity.setSession(null);
        }

        entity.setStartedAt(dto.getStartedAt());
        entity.setClosedAt(dto.getClosedAt());

        Trip updated = repository.save(entity);
        return TripMapper.toDto(updated);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        if (!repository.existsById(id)) {
            throw new EntityNotFoundException("Trip", "id", id);
        }
        repository.deleteById(id);
    }
}