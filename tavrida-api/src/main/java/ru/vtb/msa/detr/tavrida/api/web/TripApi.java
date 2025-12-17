package ru.vtb.msa.detr.tavrida.api.web;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
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
    ResponseEntity<TripDto> getById(
        @Parameter(description = "ID рейса", example = "1")
        @PathVariable("id") Long id);

    @Operation(summary = "Получить все рейсы по ID маршрута")
    @GetMapping("/v1/trips/route/{routeId}")
    ResponseEntity<List<TripDto>> getByRouteId(
        @Parameter(description = "ID маршрута", example = "1")
        @PathVariable("routeId") Long routeId);

    @Operation(summary = "Получить все рейсы по ID сессии")
    @GetMapping("/v1/trips/session/{sessionId}")
    ResponseEntity<List<TripDto>> getBySessionId(
        @Parameter(description = "ID сессии", example = "123e4567-e89b-42d3-a456-556642440000")
        @PathVariable("sessionId") UUID sessionId);

    @Operation(summary = "Создать новый рейс")
    @PostMapping("/v1/trips")
    ResponseEntity<TripDto> create(@RequestBody TripDto dto);

    @Operation(summary = "Обновить рейс")
    @PutMapping("/v1/trips/{id}")
    ResponseEntity<TripDto> update(
        @Parameter(description = "ID рейса", example = "1")
        @PathVariable("id") Long id, @RequestBody TripDto dto);

    @Operation(summary = "Удалить рейс")
    @DeleteMapping("/v1/trips/{id}")
    ResponseEntity<Void> delete(
        @Parameter(description = "ID рейса", example = "1")
        @PathVariable("id") Long id);
}