package ru.vtb.msa.detr.tavrida.core.service;

import org.springframework.stereotype.Service;
import ru.vtb.msa.detr.tavrida.core.model.mapers.TavridaMapper;
import ru.vtb.msa.detr.tavrida.api.model.CarrierDto;
import ru.vtb.msa.detr.tavrida.core.exception.EntityNotFoundException;
import ru.vtb.msa.detr.tavrida.core.model.Carrier;
import ru.vtb.msa.detr.tavrida.core.repo.CarrierRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CarrierService {

    private final CarrierRepository carrierRepository;

    public CarrierService(CarrierRepository carrierRepository) {
        this.carrierRepository = carrierRepository;
    }

    public List<CarrierDto> getAllCarriers() {
        return carrierRepository.findAll().stream()
                .map(TavridaMapper::toCarrierDto)
                .collect(Collectors.toList());
    }

    public CarrierDto getCarrierById(Long id) {
        Carrier carrier = carrierRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Carrier not found: " + id));
        return TavridaMapper.toCarrierDto(carrier);
    }

    public CarrierDto createCarrier(CarrierDto dto) {
        Carrier carrier = TavridaMapper.toCarrierEntity(dto);
        Carrier saved = carrierRepository.save(carrier);
        return TavridaMapper.toCarrierDto(saved);
    }

    public CarrierDto updateCarrier(Long id, CarrierDto dto) {
        Carrier existing = carrierRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Carrier not found: " + id));
        existing.setCarrierName(dto.getCarrierName());
        Carrier updated = carrierRepository.save(existing);
        return TavridaMapper.toCarrierDto(updated);
    }

    public void deleteCarrier(Long id) {
        if (!carrierRepository.existsById(id)) {
            throw new EntityNotFoundException("Carrier not found: " + id);
        }
        carrierRepository.deleteById(id);
    }
}