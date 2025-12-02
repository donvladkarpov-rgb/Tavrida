package ru.vtb.msa.detr.tavrida.api.web;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.vtb.msa.detr.tavrida.api.model.terminal.TerminalDeductRequest;

import java.util.List;
import java.util.Set;
import java.util.UUID;

@Tag(name = "Terminal Operations API", description = "Операции терминала: списание, проверка чёрного списка")
public interface TerminalOperationApi {

    @Operation(summary = "Списание поездки с карты")
    @PostMapping("/terminal/deduct")
    ResponseEntity<Boolean> deductTrip(@RequestBody TerminalDeductRequest request);

    @Operation(summary = "Списание поездки с карт")
    @PostMapping("/terminal/deducts")
    ResponseEntity<List<UUID>> deductsTrip(@RequestBody List<TerminalDeductRequest> request);

    @Operation(summary = "Проверить, заблокирована ли карта")
    @GetMapping("/terminal/blacklist/check/{cardGuid}")
    ResponseEntity<Boolean> isCardBlocked(
            @Parameter(description = "GUID карты", example = "123e4567-e89b-42d3-a456-556642440000")
            @PathVariable("cardGuid") UUID cardGuid);

    @Operation(summary = "Получить полный чёрный список")
    @GetMapping("/terminal/blacklist")
    ResponseEntity<Set<UUID>> getFullBlackList();
}