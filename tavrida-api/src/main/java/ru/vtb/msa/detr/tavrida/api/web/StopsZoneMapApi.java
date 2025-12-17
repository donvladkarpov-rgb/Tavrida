package ru.vtb.msa.detr.tavrida.api.web;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.vtb.msa.detr.tavrida.api.model.stop.StopsZoneMapDto;

import java.util.List;

@Tag(name = "Stops-Zone Map", description = "Управление привязкой остановок к тарифным зонам")
@RequestMapping("/v1/stops-zone-map")
public interface StopsZoneMapApi {

    @Operation(summary = "Получить привязку по ID")
    @ApiResponse(responseCode = "200", description = "Успешно", content = @Content(schema = @Schema(implementation = StopsZoneMapDto.class)))
    @ApiResponse(responseCode = "404", description = "Не найдено")
    @GetMapping("/{id}")
    ResponseEntity<StopsZoneMapDto> findById(@Parameter(description = "ID привязки", example = "1001") @PathVariable("id") Long id);

    @Operation(summary = "Получить все привязки")
    @ApiResponse(responseCode = "200", description = "Список привязок", content = @Content(schema = @Schema(implementation = StopsZoneMapDto.class)))
    @GetMapping
    ResponseEntity<List<StopsZoneMapDto>> findAll();

    @Operation(summary = "Создать новую привязку остановки к зоне")
    @ApiResponse(responseCode = "201", description = "Создано", content = @Content(schema = @Schema(implementation = StopsZoneMapDto.class)))
    @ApiResponse(responseCode = "400", description = "Некорректные данные или дубликат")
    @PostMapping
    ResponseEntity<StopsZoneMapDto> create(@io.swagger.v3.oas.annotations.parameters.RequestBody(description = "DTO привязки", required = true) @RequestBody StopsZoneMapDto dto);

    @Operation(summary = "Обновить привязку по ID")
    @ApiResponse(responseCode = "200", description = "Обновлено", content = @Content(schema = @Schema(implementation = StopsZoneMapDto.class)))
    @ApiResponse(responseCode = "400", description = "Некорректные данные или конфликт")
    @ApiResponse(responseCode = "404", description = "Не найдено")
    @PutMapping("/{id}")
    ResponseEntity<StopsZoneMapDto> update(
            @Parameter(description = "ID привязки", example = "1001") @PathVariable("id") Long id,
            @RequestBody StopsZoneMapDto dto
    );

    @Operation(summary = "Удалить привязку по ID")
    @ApiResponse(responseCode = "204", description = "Успешно удалено")
    @ApiResponse(responseCode = "404", description = "Не найдено")
    @DeleteMapping("/{id}")
    ResponseEntity<Void> deleteById(@Parameter(description = "ID привязки", example = "1001") @PathVariable("id") Long id);
}