package ru.vtb.msa.detr.tavrida.api.web;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.vtb.msa.detr.tavrida.api.model.carrier.CarrierZoneMapDto;

import java.util.List;

@Tag(name = "Carrier Zone Maps", description = "Управление привязкой перевозчиков к тарифным зонам")
public interface CarrierZoneMapApi {

    @Operation(summary = "Получить все привязки перевозчиков к зонам")
    @GetMapping("/api/v1/carrier-zone-maps")
    ResponseEntity<List<CarrierZoneMapDto>> getAll();

    @Operation(summary = "Получить привязку по ID")
    @GetMapping("/api/v1/carrier-zone-maps/{id}")
    ResponseEntity<CarrierZoneMapDto> getById(@PathVariable Long id);

    @Operation(summary = "Получить привязку по ID перевозчика и ID зоны")
    @GetMapping("/api/v1/carrier-zone-maps/carrier/{carrierId}/zone/{zoneId}")
    ResponseEntity<CarrierZoneMapDto> getByCarrierAndZone(
            @PathVariable Long carrierId,
            @PathVariable Long zoneId
    );

    @Operation(summary = "Получить все привязки по ID перевозчика")
    @GetMapping("/api/v1/carrier-zone-maps/carrier/{carrierId}")
    ResponseEntity<List<CarrierZoneMapDto>> getByCarrierId(@PathVariable Long carrierId);

    @Operation(summary = "Получить все привязки по ID зоны")
    @GetMapping("/api/v1/carrier-zone-maps/zone/{zoneId}")
    ResponseEntity<List<CarrierZoneMapDto>> getByZoneId(@PathVariable Long zoneId);

    @Operation(summary = "Создать новую привязку")
    @PostMapping("/api/v1/carrier-zone-maps")
    ResponseEntity<CarrierZoneMapDto> create(@RequestBody CarrierZoneMapDto dto);

    @Operation(summary = "Обновить привязку")
    @PutMapping("/api/v1/carrier-zone-maps/{id}")
    ResponseEntity<CarrierZoneMapDto> update(@PathVariable Long id, @RequestBody CarrierZoneMapDto dto);

    @Operation(summary = "Удалить привязку")
    @DeleteMapping("/api/v1/carrier-zone-maps/{id}")
    ResponseEntity<Void> delete(@PathVariable Long id);
}