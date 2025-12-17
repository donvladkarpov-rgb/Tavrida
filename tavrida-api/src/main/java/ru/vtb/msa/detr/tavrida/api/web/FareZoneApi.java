package ru.vtb.msa.detr.tavrida.api.web;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.vtb.msa.detr.tavrida.api.model.carrier.FareZoneDto;

import java.util.List;

@Tag(name = "Fare Zones", description = "Управление тарифными зонами")
public interface FareZoneApi {

    @Operation(summary = "Получить все тарифные зоны")
    @GetMapping("/v1/fare-zones")
    ResponseEntity<List<FareZoneDto>> getAll();

    @Operation(summary = "Получить тарифную зону по ID")
    @GetMapping("/v1/fare-zones/{id}")
    ResponseEntity<FareZoneDto> getById(@PathVariable Long id);

    @Operation(summary = "Получить тарифную зону по коду")
    @GetMapping("/v1/fare-zones/code/{zoneCode}")
    ResponseEntity<FareZoneDto> getByZoneCode(@PathVariable String zoneCode);

    @Operation(summary = "Получить тарифную зону по названию")
    @GetMapping("/v1/fare-zones/name/{zoneName}")
    ResponseEntity<FareZoneDto> getByZoneName(@PathVariable String zoneName);

    @Operation(summary = "Создать новую тарифную зону")
    @PostMapping("/v1/fare-zones")
    ResponseEntity<FareZoneDto> create(@RequestBody FareZoneDto dto);

    @Operation(summary = "Обновить тарифную зону")
    @PutMapping("/v1/fare-zones/{id}")
    ResponseEntity<FareZoneDto> update(@PathVariable Long id, @RequestBody FareZoneDto dto);

    @Operation(summary = "Удалить тарифную зону")
    @DeleteMapping("/v1/fare-zones/{id}")
    ResponseEntity<Void> delete(@PathVariable Long id);
}