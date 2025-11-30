package ru.vtb.msa.detr.tavrida.core.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.vtb.msa.detr.tavrida.api.model.ServiceEventTypeDto;
import ru.vtb.msa.detr.tavrida.core.exception.EntityNotFoundException;
import ru.vtb.msa.detr.tavrida.core.model.ServiceEventType;
import ru.vtb.msa.detr.tavrida.core.model.mapper.TavridaMapper;
import ru.vtb.msa.detr.tavrida.core.repo.ServiceEventTypeRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class ServiceEventTypeService {

    private final ServiceEventTypeRepository serviceEventTypeRepository;

    public ServiceEventTypeService(ServiceEventTypeRepository serviceEventTypeRepository) {
        this.serviceEventTypeRepository = serviceEventTypeRepository;
    }

    public List<ServiceEventTypeDto> getAllEventTypes() {
        return serviceEventTypeRepository.findAll().stream()
                .map(TavridaMapper::toServiceEventTypeDto)
                .collect(Collectors.toList());
    }

    public ServiceEventTypeDto getEventType(String eventType) {
        ServiceEventType type = serviceEventTypeRepository.findById(eventType)
                .orElseThrow(() -> new EntityNotFoundException("Event type not found: " + eventType));
        return TavridaMapper.toServiceEventTypeDto(type);
    }

    @Transactional
    public ServiceEventTypeDto createEventType(ServiceEventTypeDto dto) {
        ServiceEventType type = TavridaMapper.toServiceEventTypeEntity(dto);
        ServiceEventType saved = serviceEventTypeRepository.save(type);
        return TavridaMapper.toServiceEventTypeDto(saved);
    }

    @Transactional
    public ServiceEventTypeDto updateEventType(String eventType, ServiceEventTypeDto dto) {
        ServiceEventType existing = serviceEventTypeRepository.findById(eventType)
                .orElseThrow(() -> new EntityNotFoundException("Event type not found: " + eventType));
        existing.setEventTypeName(dto.getEventTypeName());
        ServiceEventType updated = serviceEventTypeRepository.save(existing);
        return TavridaMapper.toServiceEventTypeDto(updated);
    }

    @Transactional
    public void deleteEventType(String eventType) {
        if (!serviceEventTypeRepository.existsById(eventType)) {
            throw new EntityNotFoundException("Event type not found: " + eventType);
        }
        serviceEventTypeRepository.deleteById(eventType);
    }
}