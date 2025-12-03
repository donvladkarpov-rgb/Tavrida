package ru.vtb.msa.detr.tavrida.api.web;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.vtb.msa.detr.tavrida.api.model.*;
import ru.vtb.msa.detr.tavrida.api.model.admin.*;

/**
 * REST API для административных операций: регистрация перевозчиков, транспорта,
 * терминалов, пользователей и транспортных карт.
 */
@Tag(name = "Admin API", description = "Администрирование карточной транспортной системы Tavrida ")
public interface AdminApi {

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