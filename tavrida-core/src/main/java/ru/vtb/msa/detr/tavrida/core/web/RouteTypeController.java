package ru.vtb.msa.detr.tavrida.core.web;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import ru.vtb.msa.detr.tavrida.api.web.RouteTypeApi;
import ru.vtb.msa.detr.tavrida.core.service.RouteTypeService;
import ru.vtb.msa.detr.tavrida.api.model.route.RouteTypeDto;

import java.util.List;

@RestController
public class RouteTypeController implements RouteTypeApi {

    private final RouteTypeService routeTypeService;

    public RouteTypeController(RouteTypeService routeTypeService) {
        this.routeTypeService = routeTypeService;
    }

    @Override
    public ResponseEntity<List<RouteTypeDto>> getAll() {
        return ResponseEntity.ok(routeTypeService.findAll());
    }

    @Override
    public ResponseEntity<RouteTypeDto> getById(Long id) {
        return ResponseEntity.ok(routeTypeService.findById(id));
    }

    @Override
    public ResponseEntity<RouteTypeDto> getByName(String name) {
        return ResponseEntity.ok(routeTypeService.findByName(name));
    }

    @Override
    public ResponseEntity<RouteTypeDto> create(RouteTypeDto dto) {
        RouteTypeDto saved = routeTypeService.create(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    @Override
    public ResponseEntity<RouteTypeDto> update(Long id, RouteTypeDto dto) {
        RouteTypeDto updated = routeTypeService.update(id, dto);
        return ResponseEntity.ok(updated);
    }

    @Override
    public ResponseEntity<Void> delete(Long id) {
        routeTypeService.delete(id);
        return ResponseEntity.noContent().build();
    }
}