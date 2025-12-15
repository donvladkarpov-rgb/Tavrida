package ru.vtb.msa.detr.tavrida.core.web;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import ru.vtb.msa.detr.tavrida.api.web.CarrierZoneMapApi;
import ru.vtb.msa.detr.tavrida.core.service.CarrierZoneMapService;
import ru.vtb.msa.detr.tavrida.api.model.carrier.CarrierZoneMapDto;

import java.util.List;

@RestController
public class CarrierZoneMapController implements CarrierZoneMapApi {

    private final CarrierZoneMapService carrierZoneMapService;

    public CarrierZoneMapController(CarrierZoneMapService carrierZoneMapService) {
        this.carrierZoneMapService = carrierZoneMapService;
    }

    @Override
    public ResponseEntity<List<CarrierZoneMapDto>> getAll() {
        return ResponseEntity.ok(carrierZoneMapService.findAll());
    }

    @Override
    public ResponseEntity<CarrierZoneMapDto> getById(Long id) {
        return ResponseEntity.ok(carrierZoneMapService.findById(id));
    }

    @Override
    public ResponseEntity<CarrierZoneMapDto> getByCarrierAndZone(Long carrierId, Long zoneId) {
        return ResponseEntity.ok(carrierZoneMapService.findByCarrierIdAndZoneId(carrierId, zoneId));
    }

    @Override
    public ResponseEntity<List<CarrierZoneMapDto>> getByCarrierId(Long carrierId) {
        return ResponseEntity.ok(carrierZoneMapService.findByCarrierId(carrierId));
    }

    @Override
    public ResponseEntity<List<CarrierZoneMapDto>> getByZoneId(Long zoneId) {
        return ResponseEntity.ok(carrierZoneMapService.findByZoneId(zoneId));
    }

    @Override
    public ResponseEntity<CarrierZoneMapDto> create(CarrierZoneMapDto dto) {
        CarrierZoneMapDto saved = carrierZoneMapService.create(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    @Override
    public ResponseEntity<CarrierZoneMapDto> update(Long id, CarrierZoneMapDto dto) {
        CarrierZoneMapDto updated = carrierZoneMapService.update(id, dto);
        return ResponseEntity.ok(updated);
    }

    @Override
    public ResponseEntity<Void> delete(Long id) {
        carrierZoneMapService.delete(id);
        return ResponseEntity.noContent().build();
    }
}