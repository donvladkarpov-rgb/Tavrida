package ru.vtb.msa.detr.tavrida.api.web;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.vtb.msa.detr.tavrida.api.model.stop.TransportStopDto;

import java.util.List;

@Tag(name = "Transport Stops", description = "Управление остановками транспорта")
public interface TransportStopApi {

    @Operation(summary = "Получить все остановки")
    @GetMapping("/v1/transport-stops")
    ResponseEntity<List<TransportStopDto>> getAll();

    @Operation(summary = "Получить остановку по ID")
    @GetMapping("/v1/transport-stops/{id}")
    ResponseEntity<TransportStopDto> getById(@PathVariable Long id);

    @Operation(summary = "Получить остановку по коду")
    @GetMapping("/v1/transport-stops/code/{stopCode}")
    ResponseEntity<TransportStopDto> getByStopCode(@PathVariable String stopCode);

    @Operation(summary = "Получить все остановки по ID тарифной зоны")
    @GetMapping("/v1/transport-stops/zone/{zoneId}")
    ResponseEntity<List<TransportStopDto>> getByZoneId(@PathVariable Long zoneId);

    @Operation(summary = "Получить все активные остановки")
    @GetMapping("/v1/transport-stops/active")
    ResponseEntity<List<TransportStopDto>> getActiveStops();

    @Operation(summary = "Создать новую остановку")
    @PostMapping("/v1/transport-stops")
    ResponseEntity<TransportStopDto> create(@RequestBody TransportStopDto dto);

    @Operation(summary = "Обновить остановку")
    @PutMapping("/v1/transport-stops/{id}")
    ResponseEntity<TransportStopDto> update(@PathVariable Long id, @RequestBody TransportStopDto dto);

    @Operation(summary = "Удалить остановку")
    @DeleteMapping("/v1/transport-stops/{id}")
    ResponseEntity<Void> delete(@PathVariable Long id);
}