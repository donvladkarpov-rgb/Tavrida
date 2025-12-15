package ru.vtb.msa.detr.tavrida.core.web;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import ru.vtb.msa.detr.tavrida.api.web.FareZoneApi;
import ru.vtb.msa.detr.tavrida.core.service.FareZoneService;
import ru.vtb.msa.detr.tavrida.api.model.carrier.FareZoneDto;

import java.util.List;

@RestController
public class FareZoneController implements FareZoneApi {

    private final FareZoneService fareZoneService;

    public FareZoneController(FareZoneService fareZoneService) {
        this.fareZoneService = fareZoneService;
    }

    @Override
    public ResponseEntity<List<FareZoneDto>> getAll() {
        return ResponseEntity.ok(fareZoneService.findAll());
    }

    @Override
    public ResponseEntity<FareZoneDto> getById(Long id) {
        return ResponseEntity.ok(fareZoneService.findById(id));
    }

    @Override
    public ResponseEntity<FareZoneDto> getByZoneCode(String zoneCode) {
        return ResponseEntity.ok(fareZoneService.findByZoneCode(zoneCode));
    }

    @Override
    public ResponseEntity<FareZoneDto> getByZoneName(String zoneName) {
        return ResponseEntity.ok(fareZoneService.findByZoneName(zoneName));
    }

    @Override
    public ResponseEntity<FareZoneDto> create(FareZoneDto dto) {
        FareZoneDto saved = fareZoneService.create(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    @Override
    public ResponseEntity<FareZoneDto> update(Long id, FareZoneDto dto) {
        FareZoneDto updated = fareZoneService.update(id, dto);
        return ResponseEntity.ok(updated);
    }

    @Override
    public ResponseEntity<Void> delete(Long id) {
        fareZoneService.delete(id);
        return ResponseEntity.noContent().build();
    }
}