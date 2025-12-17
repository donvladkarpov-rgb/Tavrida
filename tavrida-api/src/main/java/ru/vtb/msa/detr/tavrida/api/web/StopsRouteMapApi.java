package ru.vtb.msa.detr.tavrida.api.web;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.vtb.msa.detr.tavrida.api.model.stop.StopsRouteMapDto;

import java.util.List;

@Tag(name = "Stops Route Maps", description = "Управление привязкой остановок к маршрутам")
public interface StopsRouteMapApi {

    @Operation(summary = "Получить все привязки остановок к маршрутам")
    @GetMapping("/v1/stops-route-maps")
    ResponseEntity<List<StopsRouteMapDto>> getAll();

    @Operation(summary = "Получить привязку по ID")
    @GetMapping("/v1/stops-route-maps/{id}")
    ResponseEntity<StopsRouteMapDto> getById(@PathVariable Long id);

    @Operation(summary = "Получить все привязки по ID маршрута")
    @GetMapping("/v1/stops-route-maps/route/{routeId}")
    ResponseEntity<List<StopsRouteMapDto>> getByRouteId(@PathVariable Long routeId);

    @Operation(summary = "Получить все привязки по ID остановки")
    @GetMapping("/v1/stops-route-maps/stop/{stopId}")
    ResponseEntity<List<StopsRouteMapDto>> getByStopId(@PathVariable Long stopId);

    @Operation(summary = "Создать новую привязку")
    @PostMapping("/v1/stops-route-maps")
    ResponseEntity<StopsRouteMapDto> create(@RequestBody StopsRouteMapDto dto);

    @Operation(summary = "Обновить привязку")
    @PutMapping("/v1/stops-route-maps/{id}")
    ResponseEntity<StopsRouteMapDto> update(@PathVariable Long id, @RequestBody StopsRouteMapDto dto);

    @Operation(summary = "Удалить привязку")
    @DeleteMapping("/v1/stops-route-maps/{id}")
    ResponseEntity<Void> delete(@PathVariable Long id);
}