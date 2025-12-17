package ru.vtb.msa.detr.tavrida.api.web;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.vtb.msa.detr.tavrida.api.model.trip.TripDto;

import java.util.List;
import java.util.UUID;

@Tag(name = "Trips", description = "Управление рейсами водителей")
public interface TripApi {

    @Operation(summary = "Получить все рейсы")
    @GetMapping("/v1/trips")
    ResponseEntity<List<TripDto>> getAll();

    @Operation(summary = "Получить рейс по ID")
    @GetMapping("/v1/trips/{id}")
    ResponseEntity<TripDto> getById(@PathVariable Long id);

    @Operation(summary = "Получить все рейсы по ID маршрута")
    @GetMapping("/v1/trips/route/{routeId}")
    ResponseEntity<List<TripDto>> getByRouteId(@PathVariable Long routeId);

    @Operation(summary = "Получить все рейсы по ID сессии")
    @GetMapping("/v1/trips/session/{sessionId}")
    ResponseEntity<List<TripDto>> getBySessionId(@PathVariable UUID sessionId);

    @Operation(summary = "Создать новый рейс")
    @PostMapping("/v1/trips")
    ResponseEntity<TripDto> create(@RequestBody TripDto dto);

    @Operation(summary = "Обновить рейс")
    @PutMapping("/v1/trips/{id}")
    ResponseEntity<TripDto> update(@PathVariable Long id, @RequestBody TripDto dto);

    @Operation(summary = "Удалить рейс")
    @DeleteMapping("/v1/trips/{id}")
    ResponseEntity<Void> delete(@PathVariable Long id);
}