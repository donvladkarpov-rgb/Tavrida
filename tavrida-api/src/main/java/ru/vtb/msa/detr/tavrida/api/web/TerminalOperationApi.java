package ru.vtb.msa.detr.tavrida.api.web;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.vtb.msa.detr.tavrida.api.model.terminal.*;

import java.util.List;
import java.util.Set;
import java.util.UUID;

@Tag(name = "Terminal Operations API", description = "Операции терминала: списание, проверка чёрного списка")
@RequestMapping("/v1/terminal/operation")
public interface TerminalOperationApi {

    @Operation(summary = "Активация водительской смены (начало сессии)")
    @PostMapping("/driver-session/start")
    ResponseEntity<DriverSessionResponse> startDriverSession(@RequestBody DriverSessionStartRequest request);

    @Operation(summary = "Деактивация водительской смены (конец сессии)")
    @PostMapping("/driver-session/stop")
    ResponseEntity<DriverSessionResponse> stopDriverSession(@RequestBody DriverSessionStopRequest request);

    @Operation(summary = "Активация водительского рейса")
    @PostMapping("/driver-trip/start")
    ResponseEntity<DriverTripResponse> startDriverTrip(@RequestBody DriverTripStartRequest request);

    @Operation(summary = "Деактивация водительского рейса")
    @PostMapping("/driver-trip/stop")
    ResponseEntity<DriverTripResponse> stopDriverTrip(@RequestBody DriverTripStopRequest request);

    @Operation(summary = "Активация терминала")
    @PostMapping("/activate")
    ResponseEntity<TerminalActivationResponse> activateTerminal(TerminalActivationRequest request);

    @PostMapping("/deactivate")
    ResponseEntity<TerminalActivationResponse> deactivateTerminal(@RequestBody TerminalDeactivationRequest request);

    @Operation(summary = "Списание поездки с карты")
    @PostMapping("/deduct")
    ResponseEntity<TerminalDeductResponse> deductTrip(@RequestBody TerminalDeductRequest request);

    @Operation(summary = "Списание поездки с карт")
    @PostMapping("deducts")
    ResponseEntity<List<UUID>> deductsTrip(@RequestBody List<TerminalDeductRequest> request);

    @Operation(summary = "Проверить, заблокирована ли карта")
    @GetMapping("/blacklist/check/{cardGuid}")
    ResponseEntity<Boolean> isCardBlocked(
            @Parameter(description = "GUID карты", example = "123e4567-e89b-42d3-a456-556642440000")
            @PathVariable("cardGuid") UUID cardGuid);

    @Operation(summary = "Получить полный чёрный список")
    @GetMapping("/blacklist")
    ResponseEntity<Set<UUID>> getFullBlackList();
}