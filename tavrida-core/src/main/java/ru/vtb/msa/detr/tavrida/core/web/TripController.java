package ru.vtb.msa.detr.tavrida.core.web;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import ru.vtb.msa.detr.tavrida.api.web.TripApi;
import ru.vtb.msa.detr.tavrida.core.service.TripService;
import ru.vtb.msa.detr.tavrida.api.model.trip.TripDto;

import java.util.List;
import java.util.UUID;

@RestController
public class TripController implements TripApi {

    private final TripService tripService;

    public TripController(TripService tripService) {
        this.tripService = tripService;
    }

    @Override
    public ResponseEntity<List<TripDto>> getAll() {
        return ResponseEntity.ok(tripService.findAll());
    }

    @Override
    public ResponseEntity<TripDto> getById(Long id) {
        return ResponseEntity.ok(tripService.findById(id));
    }

    @Override
    public ResponseEntity<List<TripDto>> getByRouteId(Long routeId) {
        return ResponseEntity.ok(tripService.findByRouteId(routeId));
    }

    @Override
    public ResponseEntity<List<TripDto>> getBySessionId(UUID sessionId) {
        return ResponseEntity.ok(tripService.findBySessionId(sessionId));
    }

    @Override
    public ResponseEntity<TripDto> create(TripDto dto) {
        TripDto saved = tripService.create(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    @Override
    public ResponseEntity<TripDto> update(Long id, TripDto dto) {
        TripDto updated = tripService.update(id, dto);
        return ResponseEntity.ok(updated);
    }

    @Override
    public ResponseEntity<Void> delete(Long id) {
        tripService.delete(id);
        return ResponseEntity.noContent().build();
    }
}