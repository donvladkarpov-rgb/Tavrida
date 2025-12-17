package ru.vtb.msa.detr.tavrida.api.web;

import io.swagger.v3.oas.annotations.Operation;
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
    ResponseEntity<RouteDto> getById(@PathVariable Long id);

    @Operation(summary = "Получить маршрут по GUID")
    @GetMapping("/v1/routes/guid/{guid}")
    ResponseEntity<RouteDto> getByGuid(@PathVariable UUID guid);

    @Operation(summary = "Получить корневые маршруты (без родителя)")
    @GetMapping("/v1/routes/root")
    ResponseEntity<List<RouteDto>> getRootRoutes();

    @Operation(summary = "Получить дочерние пути по ID родительского маршрута")
    @GetMapping("/v1/routes/{parentId}/children")
    ResponseEntity<List<RouteDto>> getChildren(@PathVariable Long parentId);

    @Operation(summary = "Создать новый маршрут")
    @PostMapping("/v1/routes")
    ResponseEntity<RouteDto> create(@RequestBody RouteDto dto);

    @Operation(summary = "Обновить маршрут")
    @PutMapping("/v1/routes/{id}")
    ResponseEntity<RouteDto> update(@PathVariable Long id, @RequestBody RouteDto dto);

    @Operation(summary = "Удалить маршрут")
    @DeleteMapping("/v1/routes/{id}")
    ResponseEntity<Void> delete(@PathVariable Long id);
}