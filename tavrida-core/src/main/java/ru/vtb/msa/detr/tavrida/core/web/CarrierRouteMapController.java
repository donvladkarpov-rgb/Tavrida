package ru.vtb.msa.detr.tavrida.core.web;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import ru.vtb.msa.detr.tavrida.api.web.CarrierRouteMapApi;
import ru.vtb.msa.detr.tavrida.core.service.CarrierRouteMapService;
import ru.vtb.msa.detr.tavrida.api.model.carrier.CarrierRouteMapDto;

import java.util.List;

@RestController
public class CarrierRouteMapController implements CarrierRouteMapApi {

    private final CarrierRouteMapService carrierRouteMapService;

    public CarrierRouteMapController(CarrierRouteMapService carrierRouteMapService) {
        this.carrierRouteMapService = carrierRouteMapService;
    }

    @Override
    public ResponseEntity<List<CarrierRouteMapDto>> getAll() {
        return ResponseEntity.ok(carrierRouteMapService.findAll());
    }

    @Override
    public ResponseEntity<CarrierRouteMapDto> getById(Long id) {
        return ResponseEntity.ok(carrierRouteMapService.findById(id));
    }

    @Override
    public ResponseEntity<List<CarrierRouteMapDto>> getByCarrierId(Long carrierId) {
        return ResponseEntity.ok(carrierRouteMapService.findByCarrierId(carrierId));
    }

    @Override
    public ResponseEntity<List<CarrierRouteMapDto>> getByRouteId(Long routeId) {
        return ResponseEntity.ok(carrierRouteMapService.findByRouteId(routeId));
    }

    @Override
    public ResponseEntity<CarrierRouteMapDto> create(CarrierRouteMapDto dto) {
        CarrierRouteMapDto saved = carrierRouteMapService.create(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    @Override
    public ResponseEntity<CarrierRouteMapDto> update(Long id, CarrierRouteMapDto dto) {
        CarrierRouteMapDto updated = carrierRouteMapService.update(id, dto);
        return ResponseEntity.ok(updated);
    }

    @Override
    public ResponseEntity<Void> delete(Long id) {
        carrierRouteMapService.delete(id);
        return ResponseEntity.noContent().build();
    }
}