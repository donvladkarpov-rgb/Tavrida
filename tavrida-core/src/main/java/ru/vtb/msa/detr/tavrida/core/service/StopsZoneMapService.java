package ru.vtb.msa.detr.tavrida.core.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.vtb.msa.detr.tavrida.api.model.stop.StopsZoneMapDto;
import ru.vtb.msa.detr.tavrida.core.exception.EntityAlreadyExistsException;
import ru.vtb.msa.detr.tavrida.core.model.StopsZoneMap;
import ru.vtb.msa.detr.tavrida.core.repo.StopsZoneMapRepository;
import ru.vtb.msa.detr.tavrida.core.model.mapper.StopsZoneMapMapper;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class StopsZoneMapService {

    private final StopsZoneMapRepository repository;

    public StopsZoneMapService(StopsZoneMapRepository repository) {
        this.repository = repository;
    }

    public StopsZoneMapDto findById(Long id) {
        return repository.findById(id)
                .map(StopsZoneMapMapper::toDto)
                .orElseThrow(() -> new NoSuchElementException("StopsZoneMap not found with id: " + id));
    }

    public List<StopsZoneMapDto> findAll() {
        return repository.findAll().stream()
                .map(StopsZoneMapMapper::toDto)
                .collect(Collectors.toList());
    }

    @Transactional
    public StopsZoneMapDto create(StopsZoneMapDto dto) {
        if (dto == null || dto.getStop() == null || dto.getZone() == null) {
            throw new IllegalArgumentException("Stop and Zone must not be null");
        }

        Long stopId = dto.getStop().getStopId();
        Long zoneId = dto.getZone().getZoneId();

        if (repository.existsByStopIdAndZoneId(stopId, zoneId)) {
            throw new EntityAlreadyExistsException(
                    "Связь остановки (ID=" + stopId + ") и тарифной зоны (ID=" + zoneId + ") уже существует"
            );
        }

        StopsZoneMap entity = StopsZoneMapMapper.toEntity(dto);
        StopsZoneMap saved = repository.save(entity);
        return StopsZoneMapMapper.toDto(saved);
    }

    @Transactional
    public StopsZoneMapDto update(Long id, StopsZoneMapDto dto) {
        // Обновление в таблице связи без составного PK — обычно не требуется,
        // так как связь stop ↔ zone — уникальна и неизменна.
        // Но если нужно — сначала удаляем, потом создаём заново.
        // Здесь реализуем как "замену", но на практике связи часто не редактируют.

        if (dto == null || dto.getStop() == null || dto.getZone() == null) {
            throw new IllegalArgumentException("Stop and Zone must not be null");
        }

        repository.deleteById(id); // удаляем старую запись

        try {
            return create(dto); // создаём новую
        } catch (EntityAlreadyExistsException e) {
            // Если новая связь уже существует — восстанавливаем старую
            StopsZoneMap old = new StopsZoneMap();
            old.setId(id);
            // Но так как мы не храним старые stop/zone — лучше не делать update
            throw new IllegalStateException("Невозможно обновить связь: новая комбинация уже существует", e);
        }
    }

    @Transactional
    public void deleteById(Long id) {
        if (!repository.existsById(id)) {
            throw new NoSuchElementException("StopsZoneMap not found with id: " + id);
        }
        repository.deleteById(id);
    }

    public boolean existsByStopAndZone(Long stopId, Long zoneId) {
        return repository.existsByStopIdAndZoneId(stopId, zoneId);
    }

}