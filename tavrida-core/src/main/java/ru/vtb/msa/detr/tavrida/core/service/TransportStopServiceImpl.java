package ru.vtb.msa.detr.tavrida.core.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.vtb.msa.detr.tavrida.core.exception.EntityNotFoundException;
import ru.vtb.msa.detr.tavrida.core.model.mapper.TransportStopMapper;
import ru.vtb.msa.detr.tavrida.core.model.FareZone;
import ru.vtb.msa.detr.tavrida.core.model.TransportStop;
import ru.vtb.msa.detr.tavrida.core.repo.FareZoneRepository;
import ru.vtb.msa.detr.tavrida.core.repo.TransportStopRepository;
import ru.vtb.msa.detr.tavrida.api.model.stop.TransportStopDto;

import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class TransportStopServiceImpl implements TransportStopService {

    private final TransportStopRepository repository;
    private final FareZoneRepository fareZoneRepository;

    public TransportStopServiceImpl(TransportStopRepository repository, FareZoneRepository fareZoneRepository) {
        this.repository = repository;
        this.fareZoneRepository = fareZoneRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public TransportStopDto findById(Long id) {
        return repository.findById(id)
                .map(TransportStopMapper::toDto)
                .orElseThrow(() -> new EntityNotFoundException("TransportStop", "id", id));
    }

    @Override
    @Transactional(readOnly = true)
    public TransportStopDto findByStopCode(String stopCode) {
        return repository.findByStopCode(stopCode)
                .map(TransportStopMapper::toDto)
                .orElseThrow(() -> new EntityNotFoundException("TransportStop", "stopCode", stopCode));
    }

    @Override
    @Transactional(readOnly = true)
    public List<TransportStopDto> findByZoneId(Long zoneId) {
        return repository.findByFareZone_ZoneId(zoneId).stream()
                .map(TransportStopMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<TransportStopDto> findActiveStops() {
        return repository.findByIsActiveTrue().stream()
                .map(TransportStopMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<TransportStopDto> findAll() {
        return repository.findAll().stream()
                .map(TransportStopMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public TransportStopDto create(TransportStopDto dto) {
        TransportStop entity = TransportStopMapper.toEntity(dto);

        // Устанавливаем тарифную зону
        if (dto.getFareZoneId() != null) {
            FareZone zone = fareZoneRepository.findById(dto.getFareZoneId())
                    .orElseThrow(() -> new EntityNotFoundException("FareZone", "id", dto.getFareZoneId()));
            entity.setFareZone(zone);
        }

        entity.setCreatedAt(Instant.now());
        entity.setUpdatedAt(Instant.now());

        TransportStop saved = repository.save(entity);
        return TransportStopMapper.toDto(saved);
    }

    @Override
    @Transactional
    public TransportStopDto update(Long id, TransportStopDto dto) {
        TransportStop entity = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("TransportStop", "id", id));

        entity.setStopCode(dto.getStopCode());
        entity.setStopName(dto.getStopName());
        entity.setStopAddress(dto.getStopAddress());
        entity.setGeoLocation(dto.getGeoLocation());
        entity.setDescription(dto.getDescription());
        entity.setActive(dto.getIsActive());
        entity.setUpdatedAt(Instant.now());

        // Обновляем тарифную зону
        if (dto.getFareZoneId() != null) {
            FareZone zone = fareZoneRepository.findById(dto.getFareZoneId())
                    .orElseThrow(() -> new EntityNotFoundException("FareZone", "id", dto.getFareZoneId()));
            entity.setFareZone(zone);
        } else {
            entity.setFareZone(null);
        }

        TransportStop updated = repository.save(entity);
        return TransportStopMapper.toDto(updated);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        if (!repository.existsById(id)) {
            throw new EntityNotFoundException("TransportStop", "id", id);
        }
        repository.deleteById(id);
    }
}