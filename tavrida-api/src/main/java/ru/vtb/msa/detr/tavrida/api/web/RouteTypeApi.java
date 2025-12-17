package ru.vtb.msa.detr.tavrida.api.web;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.vtb.msa.detr.tavrida.api.model.route.RouteTypeDto;

import java.util.List;

@Tag(name = "Route Types", description = "Управление типами маршрутов")
public interface RouteTypeApi {

    @Operation(summary = "Получить все типы маршрутов")
    @GetMapping("/v1/route-types")
    ResponseEntity<List<RouteTypeDto>> getAll();

    @Operation(summary = "Получить тип маршрута по ID")
    @GetMapping("/v1/route-types/{id}")
    ResponseEntity<RouteTypeDto> getById(
        @Parameter(description = "ID типа маршрута", example = "1")
        @PathVariable("id") Long id);

    @Operation(summary = "Получить тип маршрута по названию")
    @GetMapping("/v1/route-types/name/{name}")
    ResponseEntity<RouteTypeDto> getByName(
        @Parameter(description = "Название типа маршрута")
        @PathVariable("name") String name);

    @Operation(summary = "Создать новый тип маршрута")
    @PostMapping("/v1/route-types")
    ResponseEntity<RouteTypeDto> create(@RequestBody RouteTypeDto dto);

    @Operation(summary = "Обновить тип маршрута")
    @PutMapping("/v1/route-types/{id}")
    ResponseEntity<RouteTypeDto> update(
        @Parameter(description = "ID типа маршрута", example = "1")
        @PathVariable("id") Long id, @RequestBody RouteTypeDto dto);

    @Operation(summary = "Удалить тип маршрута")
    @DeleteMapping("/v1/route-types/{id}")
    ResponseEntity<Void> delete(
        @Parameter(description = "ID типа маршрута", example = "1")
        @PathVariable("id") Long id);
}