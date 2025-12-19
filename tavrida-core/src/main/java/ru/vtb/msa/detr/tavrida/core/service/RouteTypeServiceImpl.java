package ru.vtb.msa.detr.tavrida.core.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.vtb.msa.detr.tavrida.core.exception.EntityAlreadyExistsException;
import ru.vtb.msa.detr.tavrida.core.exception.EntityNotFoundException;
import ru.vtb.msa.detr.tavrida.core.exception.ValidationTavridaException;
import ru.vtb.msa.detr.tavrida.core.model.mapper.RouteTypeMapper;
import ru.vtb.msa.detr.tavrida.core.model.RouteType;
import ru.vtb.msa.detr.tavrida.core.repo.RouteTypeRepository;
import ru.vtb.msa.detr.tavrida.api.model.route.RouteTypeDto;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class RouteTypeServiceImpl implements RouteTypeService {

    private final RouteTypeRepository repository;

    public RouteTypeServiceImpl(RouteTypeRepository repository) {
        this.repository = repository;
    }

    @Override
    @Transactional(readOnly = true)
    public RouteTypeDto findById(Long id) {
        return repository.findById(id)
                .map(RouteTypeMapper::toDto)
                .orElseThrow(() -> new EntityNotFoundException("RouteType", "id", id));
    }

    @Override
    @Transactional(readOnly = true)
    public RouteTypeDto findByName(String name) {
        return repository.findByRouteTypesName(name)
                .map(RouteTypeMapper::toDto)
                .orElseThrow(() -> new EntityNotFoundException("RouteType", "name", name));
    }

    @Override
    @Transactional(readOnly = true)
    public List<RouteTypeDto> findAll() {
        return repository.findAll().stream()
                .map(RouteTypeMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public RouteTypeDto create(RouteTypeDto dto) {
        // Проверяем, что id не меньше 0
        if (dto.getRouteTypesId() < 0) {
            throw new ValidationTavridaException("Значение routeTypesId не может быть меньше 0.");
        }
        // Проверяем, существует ли уже тип маршрута с таким id
        if (dto.getRouteTypesId() != null && repository.existsById(dto.getRouteTypesId())) {
            throw new EntityAlreadyExistsException("Тип маршрута с id '" + dto.getRouteTypesId() + "' уже существует.");
        }
        RouteType entity = RouteTypeMapper.toEntity(dto);
        RouteType saved = repository.save(entity);
        return RouteTypeMapper.toDto(saved);
    }

    @Override
    @Transactional
    public RouteTypeDto update(Long id, RouteTypeDto dto) {
        RouteType entity = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("RouteType", "id", id));
        entity.setRouteTypesName(dto.getRouteTypesName());
        RouteType updated = repository.save(entity);
        return RouteTypeMapper.toDto(updated);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        if (!repository.existsById(id)) {
            throw new EntityNotFoundException("RouteType", "id", id);
        }
        repository.deleteById(id);
    }
}