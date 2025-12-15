package ru.vtb.msa.detr.tavrida.core.web;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import ru.vtb.msa.detr.tavrida.api.web.TransportStopApi;
import ru.vtb.msa.detr.tavrida.core.service.TransportStopService;
import ru.vtb.msa.detr.tavrida.api.model.stop.TransportStopDto;

import java.util.List;

@RestController
public class TransportStopController implements TransportStopApi {

    private final TransportStopService transportStopService;

    public TransportStopController(TransportStopService transportStopService) {
        this.transportStopService = transportStopService;
    }

    @Override
    public ResponseEntity<List<TransportStopDto>> getAll() {
        return ResponseEntity.ok(transportStopService.findAll());
    }

    @Override
    public ResponseEntity<TransportStopDto> getById(Long id) {
        return ResponseEntity.ok(transportStopService.findById(id));
    }

    @Override
    public ResponseEntity<TransportStopDto> getByStopCode(String stopCode) {
        return ResponseEntity.ok(transportStopService.findByStopCode(stopCode));
    }

    @Override
    public ResponseEntity<List<TransportStopDto>> getByZoneId(Long zoneId) {
        return ResponseEntity.ok(transportStopService.findByZoneId(zoneId));
    }

    @Override
    public ResponseEntity<List<TransportStopDto>> getActiveStops() {
        return ResponseEntity.ok(transportStopService.findActiveStops());
    }

    @Override
    public ResponseEntity<TransportStopDto> create(TransportStopDto dto) {
        TransportStopDto saved = transportStopService.create(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    @Override
    public ResponseEntity<TransportStopDto> update(Long id, TransportStopDto dto) {
        TransportStopDto updated = transportStopService.update(id, dto);
        return ResponseEntity.ok(updated);
    }

    @Override
    public ResponseEntity<Void> delete(Long id) {
        transportStopService.delete(id);
        return ResponseEntity.noContent().build();
    }
}