package ru.vtb.msa.detr.tavrida.api.web;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.vtb.msa.detr.tavrida.api.model.*;
import ru.vtb.msa.detr.tavrida.api.model.admin.*;
import ru.vtb.msa.detr.tavrida.api.model.cashier.CashierLoginRequest;
import ru.vtb.msa.detr.tavrida.api.model.cashier.CashierLoginResponse;

/**
 * REST API для административных операций: регистрация перевозчиков, транспорта,
 * терминалов, пользователей и транспортных карт.
 */
@Tag(name = "Admin API", description = "Администрирование карточной транспортной системы Tavrida ")
@RequestMapping("/v1/admin")
public interface AdminApi {

    @Operation(summary = "Вход admin в приложение")
    @PostMapping("/auth/login")
    ResponseEntity<AdminLoginResponse> adminLogin(@Valid @RequestBody AdminLoginRequest request);

    // --- Перевозчики ---
    @PostMapping("/carriers")
    ResponseEntity<CarrierDto> registerCarrier(@Valid @RequestBody CarrierRegistrationRequest request);

    // --- Транспорт ---
    @PostMapping("/transports")
    ResponseEntity<TransportDto> registerTransport(@Valid @RequestBody TransportRegistrationRequest request);

    // --- Терминалы ---
    @PostMapping("/terminals")
    ResponseEntity<TerminalDto> registerTerminal(@Valid @RequestBody TerminalRegistrationRequest request);

    // --- Пользователи ---
    @PostMapping("/users")
    ResponseEntity<UserDto> registerUser(@Valid @RequestBody UserRegistrationRequest request);

    // --- Карты ---
    @PostMapping("/cards")
    ResponseEntity<CardDto> registerCard(@Valid @RequestBody CardRegistrationRequest request);
}