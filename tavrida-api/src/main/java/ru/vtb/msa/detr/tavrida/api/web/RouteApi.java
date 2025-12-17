package ru.vtb.msa.detr.tavrida.api.web;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.vtb.msa.detr.tavrida.api.model.route.RouteDto;

import java.util.List;
import java.util.UUID;

@Tag(name = "Routes", description = "Управление маршрутами и путями")
public interface RouteApi {

    @Operation(summary = "Получить все маршруты")
    @GetMapping("/v1/routes")
    ResponseEntity<List<RouteDto>> getAll();

    @Operation(summary = "Получить маршрут по ID")
    @GetMapping("/v1/routes/{id}")
    ResponseEntity<RouteDto> getById(
        @Parameter(description = "ID маршрута", example = "1")
        @PathVariable("id") Long id);

    @Operation(summary = "Получить маршрут по GUID")
    @GetMapping("/v1/routes/guid/{guid}")
    ResponseEntity<RouteDto> getByGuid(
        @Parameter(description = "GUID маршрута", example = "123e4567-e89b-42d3-a456-556642440000")
        @PathVariable("guid") UUID guid);

    @Operation(summary = "Получить корневые маршруты (без родителя)")
    @GetMapping("/v1/routes/root")
    ResponseEntity<List<RouteDto>> getRootRoutes();

    @Operation(summary = "Получить дочерние пути по ID родительского маршрута")
    @GetMapping("/v1/routes/{parentId}/children")
    ResponseEntity<List<RouteDto>> getChildren(
        @Parameter(description = "ID родительского маршрута", example = "1")
        @PathVariable("parentId") Long parentId);

    @Operation(summary = "Создать новый маршрут")
    @PostMapping("/v1/routes")
    ResponseEntity<RouteDto> create(@RequestBody RouteDto dto);

    @Operation(summary = "Обновить маршрут")
    @PutMapping("/v1/routes/{id}")
    ResponseEntity<RouteDto> update(
        @Parameter(description = "ID маршрута", example = "1")
        @PathVariable("id") Long id, @RequestBody RouteDto dto);

    @Operation(summary = "Удалить маршрут")
    @DeleteMapping("/v1/routes/{id}")
    ResponseEntity<Void> delete(
        @Parameter(description = "ID маршрута", example = "1")
        @PathVariable("id") Long id);
}