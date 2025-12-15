package ru.vtb.msa.detr.tavrida.core.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.vtb.msa.detr.tavrida.core.exception.EntityNotFoundException;
import ru.vtb.msa.detr.tavrida.core.model.mapper.FareZoneMapper;
import ru.vtb.msa.detr.tavrida.core.model.FareZone;
import ru.vtb.msa.detr.tavrida.core.repo.FareZoneRepository;
import ru.vtb.msa.detr.tavrida.api.model.carrier.FareZoneDto;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class FareZoneServiceImpl implements FareZoneService {

    private final FareZoneRepository repository;

    public FareZoneServiceImpl(FareZoneRepository repository) {
        this.repository = repository;
    }

    @Override
    @Transactional(readOnly = true)
    public FareZoneDto findById(Long id) {
        return repository.findById(id)
                .map(FareZoneMapper::toDto)
                .orElseThrow(() -> new EntityNotFoundException("FareZone", "id", id));
    }

    @Override
    @Transactional(readOnly = true)
    public FareZoneDto findByZoneCode(String zoneCode) {
        return repository.findByZoneCode(zoneCode)
                .map(FareZoneMapper::toDto)
                .orElseThrow(() -> new EntityNotFoundException("FareZone", "zoneCode", zoneCode));
    }

    @Override
    @Transactional(readOnly = true)
    public FareZoneDto findByZoneName(String zoneName) {
        return repository.findByZoneName(zoneName)
                .map(FareZoneMapper::toDto)
                .orElseThrow(() -> new EntityNotFoundException("FareZone", "zoneName", zoneName));
    }

    @Override
    @Transactional(readOnly = true)
    public List<FareZoneDto> findAll() {
        return repository.findAll().stream()
                .map(FareZoneMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public FareZoneDto create(FareZoneDto dto) {
        FareZone entity = FareZoneMapper.toEntity(dto);
        FareZone saved = repository.save(entity);
        return FareZoneMapper.toDto(saved);
    }

    @Override
    @Transactional
    public FareZoneDto update(Long id, FareZoneDto dto) {
        FareZone entity = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("FareZone", "id", id));
        entity.setZoneCode(dto.getZoneCode());
        entity.setZoneName(dto.getZoneName());
        entity.setDescription(dto.getDescription());
        FareZone updated = repository.save(entity);
        return FareZoneMapper.toDto(updated);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        if (!repository.existsById(id)) {
            throw new EntityNotFoundException("FareZone", "id", id);
        }
        repository.deleteById(id);
    }
}