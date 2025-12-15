package ru.vtb.msa.detr.tavrida.core.web;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import ru.vtb.msa.detr.tavrida.api.web.StopsRouteMapApi;
import ru.vtb.msa.detr.tavrida.core.service.StopsRouteMapService;
import ru.vtb.msa.detr.tavrida.api.model.stop.StopsRouteMapDto;

import java.util.List;

@RestController
public class StopsRouteMapController implements StopsRouteMapApi {

    private final StopsRouteMapService stopsRouteMapService;

    public StopsRouteMapController(StopsRouteMapService stopsRouteMapService) {
        this.stopsRouteMapService = stopsRouteMapService;
    }

    @Override
    public ResponseEntity<List<StopsRouteMapDto>> getAll() {
        return ResponseEntity.ok(stopsRouteMapService.findAll());
    }

    @Override
    public ResponseEntity<StopsRouteMapDto> getById(Long id) {
        return ResponseEntity.ok(stopsRouteMapService.findById(id));
    }

    @Override
    public ResponseEntity<List<StopsRouteMapDto>> getByRouteId(Long routeId) {
        return ResponseEntity.ok(stopsRouteMapService.findByRouteId(routeId));
    }

    @Override
    public ResponseEntity<List<StopsRouteMapDto>> getByStopId(Long stopId) {
        return ResponseEntity.ok(stopsRouteMapService.findByStopId(stopId));
    }

    @Override
    public ResponseEntity<StopsRouteMapDto> create(StopsRouteMapDto dto) {
        StopsRouteMapDto saved = stopsRouteMapService.create(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    @Override
    public ResponseEntity<StopsRouteMapDto> update(Long id, StopsRouteMapDto dto) {
        StopsRouteMapDto updated = stopsRouteMapService.update(id, dto);
        return ResponseEntity.ok(updated);
    }

    @Override
    public ResponseEntity<Void> delete(Long id) {
        stopsRouteMapService.delete(id);
        return ResponseEntity.noContent().build();
    }
}