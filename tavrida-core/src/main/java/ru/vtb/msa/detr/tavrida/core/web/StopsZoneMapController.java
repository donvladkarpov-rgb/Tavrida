package ru.vtb.msa.detr.tavrida.core.web;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.vtb.msa.detr.tavrida.api.web.StopsZoneMapApi;
import ru.vtb.msa.detr.tavrida.api.model.stop.StopsZoneMapDto;
import ru.vtb.msa.detr.tavrida.core.service.StopsZoneMapService;

import java.util.List;

@RestController
public class StopsZoneMapController implements StopsZoneMapApi {

    private final StopsZoneMapService service;

    public StopsZoneMapController(StopsZoneMapService service) {
        this.service = service;
    }

    @Override
    public ResponseEntity<StopsZoneMapDto> findById(Long id) {
        return ResponseEntity.ok(service.findById(id));
    }

    @Override
    public ResponseEntity<List<StopsZoneMapDto>> findAll() {
        return ResponseEntity.ok(service.findAll());
    }

    @Override
    public ResponseEntity<StopsZoneMapDto> create(StopsZoneMapDto dto) {
        StopsZoneMapDto created = service.create(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @Override
    public ResponseEntity<StopsZoneMapDto> update(Long id, StopsZoneMapDto dto) {
        StopsZoneMapDto updated = service.update(id, dto);
        return ResponseEntity.ok(updated);
    }

    @Override
    public ResponseEntity<Void> deleteById(Long id) {
        service.deleteById(id);
        return ResponseEntity.noContent().build();
    }

}