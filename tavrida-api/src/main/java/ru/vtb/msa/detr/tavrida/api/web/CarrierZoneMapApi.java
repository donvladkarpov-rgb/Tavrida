package ru.vtb.msa.detr.tavrida.api.web;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.vtb.msa.detr.tavrida.api.model.carrier.CarrierZoneMapDto;

import java.util.List;

@Tag(name = "Carrier Zone Maps", description = "Управление привязкой перевозчиков к тарифным зонам")
public interface CarrierZoneMapApi {

    @Operation(summary = "Получить все привязки перевозчиков к зонам")
    @GetMapping("/v1/carrier-zone-maps")
    ResponseEntity<List<CarrierZoneMapDto>> getAll();

    @Operation(summary = "Получить привязку по ID")
    @GetMapping("/v1/carrier-zone-maps/{id}")
    ResponseEntity<CarrierZoneMapDto> getById(
        @Parameter(description = "ID зоны", example = "1")
        @PathVariable("id") Long id);

    @Operation(summary = "Получить привязку по ID перевозчика и ID зоны")
    @GetMapping("/v1/carrier-zone-maps/carrier/{carrierId}/zone/{zoneId}")
    ResponseEntity<CarrierZoneMapDto> getByCarrierAndZone(
        @Parameter(description = "ID перевозчика", example = "1")
        @PathVariable("carrierId") Long carrierId,
        @Parameter(description = "ID зоны", example = "1")
        @PathVariable("zoneId") Long zoneId
    );

    @Operation(summary = "Получить все привязки по ID перевозчика")
    @GetMapping("/v1/carrier-zone-maps/carrier/{carrierId}")
    ResponseEntity<List<CarrierZoneMapDto>> getByCarrierId(
        @Parameter(description = "ID перевозчика", example = "1")
        @PathVariable("carrierId") Long carrierId);

    @Operation(summary = "Получить все привязки по ID зоны")
    @GetMapping("/v1/carrier-zone-maps/zone/{zoneId}")
    ResponseEntity<List<CarrierZoneMapDto>> getByZoneId(
        @Parameter(description = "ID зоны", example = "1")
        @PathVariable("zoneId") Long zoneId);

    @Operation(summary = "Создать новую привязку")
    @PostMapping("/v1/carrier-zone-maps")
    ResponseEntity<CarrierZoneMapDto> create(@RequestBody CarrierZoneMapDto dto);

    @Operation(summary = "Обновить привязку")
    @PutMapping("/v1/carrier-zone-maps/{id}")
    ResponseEntity<CarrierZoneMapDto> update(
        @Parameter(description = "ID зоны", example = "1")
        @PathVariable("id") Long id, @RequestBody CarrierZoneMapDto dto);

    @Operation(summary = "Удалить привязку")
    @DeleteMapping("/v1/carrier-zone-maps/{id}")
    ResponseEntity<Void> delete(
        @Parameter(description = "ID зоны", example = "1")
        @PathVariable("id") Long id);
}