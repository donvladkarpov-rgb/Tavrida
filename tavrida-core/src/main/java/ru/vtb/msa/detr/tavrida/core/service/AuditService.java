package ru.vtb.msa.detr.tavrida.core.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.vtb.msa.detr.tavrida.api.model.ServiceEventDto;
import ru.vtb.msa.detr.tavrida.core.model.ServiceEvent;
import ru.vtb.msa.detr.tavrida.core.model.mapper.TavridaMapper;
import ru.vtb.msa.detr.tavrida.core.repo.ServiceEventRepository;

import java.util.List;

@Service
@Transactional
public class AuditService {

    private final ServiceEventRepository serviceEventRepository;

    public AuditService(ServiceEventRepository serviceEventRepository) {
        this.serviceEventRepository = serviceEventRepository;
    }

    public ServiceEventDto logEvent(ServiceEventDto dto) {
        ServiceEvent event = TavridaMapper.toServiceEventEntity(dto);
        ServiceEvent saved = serviceEventRepository.save(event);
        return TavridaMapper.toServiceEventDto(saved);
    }

    public List<ServiceEventDto> getEventsByUser(Long userId) {
        return serviceEventRepository.findByDoerUserId(userId).stream()
                .map(TavridaMapper::toServiceEventDto)
                .toList();
    }

    public List<ServiceEventDto> getEventsByReference(Long referenceId) {
        return serviceEventRepository.findByReferenceId(referenceId).stream()
                .map(TavridaMapper::toServiceEventDto)
                .toList();
    }
}