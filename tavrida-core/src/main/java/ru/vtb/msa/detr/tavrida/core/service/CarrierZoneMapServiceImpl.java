package ru.vtb.msa.detr.tavrida.core.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.vtb.msa.detr.tavrida.core.exception.EntityNotFoundException;
import ru.vtb.msa.detr.tavrida.core.model.mapper.CarrierZoneMapMapper;
import ru.vtb.msa.detr.tavrida.core.model.Carrier;
import ru.vtb.msa.detr.tavrida.core.model.CarrierZoneMap;
import ru.vtb.msa.detr.tavrida.core.model.FareZone;
import ru.vtb.msa.detr.tavrida.core.repo.CarrierRepository;
import ru.vtb.msa.detr.tavrida.core.repo.CarrierZoneMapRepository;
import ru.vtb.msa.detr.tavrida.core.repo.FareZoneRepository;
import ru.vtb.msa.detr.tavrida.api.model.carrier.CarrierZoneMapDto;

import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CarrierZoneMapServiceImpl implements CarrierZoneMapService {

    private final CarrierZoneMapRepository repository;
    private final CarrierRepository carrierRepository;
    private final FareZoneRepository fareZoneRepository;

    public CarrierZoneMapServiceImpl(
            CarrierZoneMapRepository repository,
            CarrierRepository carrierRepository,
            FareZoneRepository fareZoneRepository
    ) {
        this.repository = repository;
        this.carrierRepository = carrierRepository;
        this.fareZoneRepository = fareZoneRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public CarrierZoneMapDto findById(Long id) {
        return repository.findById(id)
                .map(CarrierZoneMapMapper::toDto)
                .orElseThrow(() -> new EntityNotFoundException("CarrierZoneMap", "id", id));
    }

    @Override
    @Transactional(readOnly = true)
    public CarrierZoneMapDto findByCarrierIdAndZoneId(Long carrierId, Long zoneId) {
        return repository.findByCarrier_CarrierIdAndFareZone_ZoneId(carrierId, zoneId)
                .map(CarrierZoneMapMapper::toDto)
                .orElseThrow(() -> new EntityNotFoundException("CarrierZoneMap", "carrierId=" + carrierId + ", zoneId=" + zoneId, ""));
    }

    @Override
    @Transactional(readOnly = true)
    public List<CarrierZoneMapDto> findByCarrierId(Long carrierId) {
        return repository.findByCarrier_CarrierId(carrierId).stream()
                .map(CarrierZoneMapMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<CarrierZoneMapDto> findByZoneId(Long zoneId) {
        return repository.findByFareZone_ZoneId(zoneId).stream()
                .map(CarrierZoneMapMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<CarrierZoneMapDto> findAll() {
        return repository.findAll().stream()
                .map(CarrierZoneMapMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public CarrierZoneMapDto create(CarrierZoneMapDto dto) {
        CarrierZoneMap entity = CarrierZoneMapMapper.toEntity(dto);

        // Устанавливаем перевозчика
        if (dto.getCarrierId() != null) {
            Carrier carrier = carrierRepository.findById(dto.getCarrierId())
                    .orElseThrow(() -> new EntityNotFoundException("Carrier", "id", dto.getCarrierId()));
            entity.setCarrier(carrier);
        }

        // Устанавливаем тарифную зону
        if (dto.getZoneId() != null) {
            FareZone zone = fareZoneRepository.findById(dto.getZoneId())
                    .orElseThrow(() -> new EntityNotFoundException("FareZone", "id", dto.getZoneId()));
            entity.setFareZone(zone);
        }

        entity.setCreatedAt(Instant.now());
        entity.setUpdatedAt(Instant.now());

        CarrierZoneMap saved = repository.save(entity);
        return CarrierZoneMapMapper.toDto(saved);
    }

    @Override
    @Transactional
    public CarrierZoneMapDto update(Long id, CarrierZoneMapDto dto) {
        CarrierZoneMap entity = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("CarrierZoneMap", "id", id));

        // Обновляем перевозчика
        if (dto.getCarrierId() != null) {
            Carrier carrier = carrierRepository.findById(dto.getCarrierId())
                    .orElseThrow(() -> new EntityNotFoundException("Carrier", "id", dto.getCarrierId()));
            entity.setCarrier(carrier);
        } else {
            entity.setCarrier(null);
        }

        // Обновляем зону
        if (dto.getZoneId() != null) {
            FareZone zone = fareZoneRepository.findById(dto.getZoneId())
                    .orElseThrow(() -> new EntityNotFoundException("FareZone", "id", dto.getZoneId()));
            entity.setFareZone(zone);
        } else {
            entity.setFareZone(null);
        }

        entity.setBaseFare(dto.getBaseFare());
        entity.setUpdatedAt(Instant.now());

        CarrierZoneMap updated = repository.save(entity);
        return CarrierZoneMapMapper.toDto(updated);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        if (!repository.existsById(id)) {
            throw new EntityNotFoundException("CarrierZoneMap", "id", id);
        }
        repository.deleteById(id);
    }
}