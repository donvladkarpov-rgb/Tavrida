package ru.vtb.msa.detr.tavrida.api.web;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.vtb.msa.detr.tavrida.api.model.CardPanHashDto;

import java.util.List;

@Tag(name = "Card PAN Hashes", description = "Управление хэшами PAN-номеров карт")
@RequestMapping("/v1/card-pan-hashes")
public interface CardPanHashApi {

    @Operation(summary = "Получить все хэши PAN")
    @ApiResponse(responseCode = "200", description = "Список успешно загружен")
    @GetMapping
    List<CardPanHashDto> getAll();

    @Operation(summary = "Получить хэш PAN по ID")
    @ApiResponse(responseCode = "200", description = "Хэш найден")
    @ApiResponse(responseCode = "404", description = "Хэш не найден")
    @GetMapping("/{id}")
    ResponseEntity<CardPanHashDto> getById(
            @Parameter(description = "ID хэша PAN", required = true)
            @PathVariable("id") Long id
    );

    @Operation(summary = "Получить хэш PAN по значению хэша")
    @ApiResponse(responseCode = "200", description = "Хэш найден")
    @ApiResponse(responseCode = "404", description = "Хэш не найден")
    @GetMapping("/hash/{hash}")
    ResponseEntity<CardPanHashDto> getByHash(
            @Parameter(description = "Значение хэша PAN", required = true)
            @PathVariable("hash") String hash
    );

    @Operation(summary = "Создать новый хэш PAN")
    @ApiResponse(responseCode = "200", description = "Хэш успешно создан")
    @PostMapping
    ResponseEntity<CardPanHashDto> create(@RequestBody CardPanHashDto dto);

    @Operation(summary = "Обновить хэш PAN по ID")
    @ApiResponse(responseCode = "200", description = "Хэш успешно обновлён")
    @ApiResponse(responseCode = "404", description = "Хэш не найден")
    @PutMapping("/{id}")
    ResponseEntity<CardPanHashDto> update(
            @Parameter(description = "ID хэша PAN", required = true)
            @PathVariable("id") Long id,
            @RequestBody CardPanHashDto dto
    );

    @Operation(summary = "Удалить хэш PAN по ID")
    @ApiResponse(responseCode = "204", description = "Хэш успешно удалён")
    @ApiResponse(responseCode = "404", description = "Хэш не найден")
    @DeleteMapping("/{id}")
    ResponseEntity<Void> delete(
            @Parameter(description = "ID хэша PAN", required = true)
            @PathVariable("id") Long id
    );
}