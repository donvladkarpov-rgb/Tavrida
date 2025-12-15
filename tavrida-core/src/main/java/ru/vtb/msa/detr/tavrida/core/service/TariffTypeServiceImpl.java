package ru.vtb.msa.detr.tavrida.core.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.vtb.msa.detr.tavrida.core.exception.EntityNotFoundException;
import ru.vtb.msa.detr.tavrida.core.model.mapper.TariffTypeMapper;
import ru.vtb.msa.detr.tavrida.core.model.TariffType;
import ru.vtb.msa.detr.tavrida.core.repo.TariffTypeRepository;
import ru.vtb.msa.detr.tavrida.api.model.tariff.TariffTypeDto;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class TariffTypeServiceImpl implements TariffTypeService {

    private final TariffTypeRepository repository;

    public TariffTypeServiceImpl(TariffTypeRepository repository) {
        this.repository = repository;
    }

    @Override
    @Transactional(readOnly = true)
    public TariffTypeDto findById(Integer id) {
        return repository.findById(id)
                .map(TariffTypeMapper::toDto)
                .orElseThrow(() -> new EntityNotFoundException("TariffType", "id", id));
    }

    @Override
    @Transactional(readOnly = true)
    public TariffTypeDto findByCode(String code) {
        return repository.findByCode(code)
                .map(TariffTypeMapper::toDto)
                .orElseThrow(() -> new EntityNotFoundException("TariffType", "code", code));
    }

    @Override
    @Transactional(readOnly = true)
    public List<TariffTypeDto> findAll() {
        return repository.findAll().stream()
                .map(TariffTypeMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public TariffTypeDto create(TariffTypeDto dto) {
        TariffType entity = TariffTypeMapper.toEntity(dto);
        TariffType saved = repository.save(entity);
        return TariffTypeMapper.toDto(saved);
    }

    @Override
    @Transactional
    public TariffTypeDto update(Integer id, TariffTypeDto dto) {
        TariffType entity = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("TariffType", "id", id));
        entity.setCode(dto.getCode());
        entity.setName(dto.getName());
        entity.setDescription(dto.getDescription());
        TariffType updated = repository.save(entity);
        return TariffTypeMapper.toDto(updated);
    }

    @Override
    @Transactional
    public void delete(Integer id) {
        if (!repository.existsById(id)) {
            throw new EntityNotFoundException("TariffType", "id", id);
        }
        repository.deleteById(id);
    }
}