package ru.vtb.msa.detr.tavrida.core.web;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import ru.vtb.msa.detr.tavrida.api.web.RouteApi;
import ru.vtb.msa.detr.tavrida.core.service.RouteService;
import ru.vtb.msa.detr.tavrida.api.model.route.RouteDto;

import java.util.List;
import java.util.UUID;

@RestController
public class RouteController implements RouteApi {

    private final RouteService routeService;

    public RouteController(RouteService routeService) {
        this.routeService = routeService;
    }

    @Override
    public ResponseEntity<List<RouteDto>> getAll() {
        return ResponseEntity.ok(routeService.findAll());
    }

    @Override
    public ResponseEntity<RouteDto> getById(Long id) {
        return ResponseEntity.ok(routeService.findById(id));
    }

    @Override
    public ResponseEntity<RouteDto> getByGuid(UUID guid) {
        return ResponseEntity.ok(routeService.findByRouteGuid(guid));
    }

    @Override
    public ResponseEntity<List<RouteDto>> getRootRoutes() {
        return ResponseEntity.ok(routeService.findRootRoutes());
    }

    @Override
    public ResponseEntity<List<RouteDto>> getChildren(Long parentId) {
        return ResponseEntity.ok(routeService.findChildren(parentId));
    }

    @Override
    public ResponseEntity<RouteDto> create(RouteDto dto) {
        RouteDto saved = routeService.create(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    @Override
    public ResponseEntity<RouteDto> update(Long id, RouteDto dto) {
        RouteDto updated = routeService.update(id, dto);
        return ResponseEntity.ok(updated);
    }

    @Override
    public ResponseEntity<Void> delete(Long id) {
        routeService.delete(id);
        return ResponseEntity.noContent().build();
    }
}