package ru.vtb.msa.detr.tavrida.core.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.vtb.msa.detr.tavrida.core.model.mapper.TavridaMapper;
import ru.vtb.msa.detr.tavrida.api.model.TransportDto;
import ru.vtb.msa.detr.tavrida.core.exception.EntityNotFoundException;
import ru.vtb.msa.detr.tavrida.core.model.Transport;
import ru.vtb.msa.detr.tavrida.core.repo.TransportRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class TransportService {

    private final TransportRepository transportRepository;

    public TransportService(TransportRepository transportRepository) {
        this.transportRepository = transportRepository;
    }

    public List<TransportDto> getAllTransports() {
        return transportRepository.findAll().stream()
                .map(TavridaMapper::toTransportDto)
                .collect(Collectors.toList());
    }

    public TransportDto getTransportById(Long id) {
        Transport transport = transportRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Transport not found: " + id));
        return TavridaMapper.toTransportDto(transport);
    }

    public TransportDto createTransport(TransportDto dto) {
        Transport transport = TavridaMapper.toTransportEntity(dto);
        Transport saved = transportRepository.save(transport);
        return TavridaMapper.toTransportDto(saved);
    }

    public TransportDto updateTransport(Long id, TransportDto dto) {
        Transport existing = transportRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Transport not found: " + id));
        existing.setTransportGuid(dto.getTransportGuid());
        existing.setCarrier(TavridaMapper.toCarrierEntity(dto.getCarrier()));
        existing.setTransportNumber(dto.getTransportNumber());
        existing.setTransportName(dto.getTransportName());
        Transport updated = transportRepository.save(existing);
        return TavridaMapper.toTransportDto(updated);
    }

    public void deleteTransport(Long id) {
        if (!transportRepository.existsById(id)) {
            throw new EntityNotFoundException("Transport not found: " + id);
        }
        transportRepository.deleteById(id);
    }
}