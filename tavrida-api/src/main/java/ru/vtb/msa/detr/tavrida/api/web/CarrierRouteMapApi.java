package ru.vtb.msa.detr.tavrida.api.web;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.vtb.msa.detr.tavrida.api.model.carrier.CarrierRouteMapDto;

import java.util.List;

@Tag(name = "Carrier Route Maps", description = "Управление привязкой маршрутов к перевозчикам")
public interface CarrierRouteMapApi {

    @Operation(summary = "Получить все привязки маршрутов к перевозчикам")
    @GetMapping("/v1/carrier-route-maps")
    ResponseEntity<List<CarrierRouteMapDto>> getAll();

    @Operation(summary = "Получить привязку по ID")
    @GetMapping("/v1/carrier-route-maps/{id}")
    ResponseEntity<CarrierRouteMapDto> getById(@PathVariable Long id);

    @Operation(summary = "Получить все привязки по ID перевозчика")
    @GetMapping("/v1/carrier-route-maps/carrier/{carrierId}")
    ResponseEntity<List<CarrierRouteMapDto>> getByCarrierId(@PathVariable Long carrierId);

    @Operation(summary = "Получить все привязки по ID маршрута")
    @GetMapping("/v1/carrier-route-maps/route/{routeId}")
    ResponseEntity<List<CarrierRouteMapDto>> getByRouteId(@PathVariable Long routeId);

    @Operation(summary = "Создать новую привязку")
    @PostMapping("/v1/carrier-route-maps")
    ResponseEntity<CarrierRouteMapDto> create(@RequestBody CarrierRouteMapDto dto);

    @Operation(summary = "Обновить привязку")
    @PutMapping("/v1/carrier-route-maps/{id}")
    ResponseEntity<CarrierRouteMapDto> update(@PathVariable Long id, @RequestBody CarrierRouteMapDto dto);

    @Operation(summary = "Удалить привязку")
    @DeleteMapping("/v1/carrier-route-maps/{id}")
    ResponseEntity<Void> delete(@PathVariable Long id);
}