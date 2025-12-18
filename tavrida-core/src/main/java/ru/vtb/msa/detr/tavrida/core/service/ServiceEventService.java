package ru.vtb.msa.detr.tavrida.core.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.vtb.msa.detr.tavrida.api.model.ServiceEventDto;
import ru.vtb.msa.detr.tavrida.core.model.ServiceEvent;
import ru.vtb.msa.detr.tavrida.core.model.Terminal;
import ru.vtb.msa.detr.tavrida.core.model.mapper.TavridaMapper;
import ru.vtb.msa.detr.tavrida.core.repo.ServiceEventRepository;
import ru.vtb.msa.detr.tavrida.core.repo.TerminalRepository;

import java.util.List;

@Service
@Transactional
public class ServiceEventService {

    private final ServiceEventRepository serviceEventRepository;
    private final TerminalRepository terminalRepository;

    public ServiceEventService(
            ServiceEventRepository serviceEventRepository,
            TerminalRepository terminalRepository
    ) {
        this.serviceEventRepository = serviceEventRepository;
        this.terminalRepository = terminalRepository;
    }

    public ServiceEventDto logEvent(ServiceEventDto dto) {
        Terminal terminal = terminalRepository.findById(dto.getSession().getTerminalId()).orElse(null);
        ServiceEvent event = TavridaMapper.toServiceEventEntity(dto, terminal);
        ServiceEvent saved = serviceEventRepository.save(event);
        return TavridaMapper.toServiceEventDto(saved);
    }

    public List<ServiceEventDto> getEventsByUser(Long userId) {
        return serviceEventRepository.findByUser_UserId(userId).stream()
                .map(TavridaMapper::toServiceEventDto)
                .toList();
    }

    public List<ServiceEventDto> getEventsByReference(Long referenceId) {
        return serviceEventRepository.findByReferenceId(referenceId).stream()
                .map(TavridaMapper::toServiceEventDto)
                .toList();
    }
}