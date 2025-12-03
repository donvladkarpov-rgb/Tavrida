package ru.vtb.msa.detr.tavrida.api.web;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.vtb.msa.detr.tavrida.api.model.*;

import java.util.List;
import java.util.UUID;

@Tag(name = "Tavrida Extended API", description = "API for sessions, codes, audit, payments and master password")
@RequestMapping("/api/v1/tavrida/extended")
public interface TavridaApiExtended {

    // ========== SESSIONS ==========
    @Operation(summary = "Create user session")
    @PostMapping("/sessions")
    ResponseEntity<UserSessionDto> createSession(@RequestBody UserSessionDto dto);

    @Operation(summary = "Get session by ID")
    @GetMapping("/sessions/{sessionId}")
    ResponseEntity<UserSessionDto> getSession(
            @Parameter(description = "Session UUID", example = "550e8400-e29b-41d4-a716-446655440000")
            @PathVariable("sessionId") UUID sessionId);

    @Operation(summary = "Check if session is valid")
    @GetMapping("/sessions/{sessionId}/valid")
    ResponseEntity<Boolean> isSessionValid(
            @Parameter(description = "Session UUID", example = "550e8400-e29b-41d4-a716-446655440000")
            @PathVariable("sessionId") UUID sessionId);

    // ========== CODES ==========
    @Operation(summary = "Create code")
    @PostMapping("/codes")
    ResponseEntity<CodeDto> createCode(@RequestBody CodeDto dto);

    @Operation(summary = "Get code by UID")
    @GetMapping("/codes/{codeUid}")
    ResponseEntity<CodeDto> getCode(
            @Parameter(description = "Code UID (16 chars)", example = "A1B2C3D4E5F6G7H8")
            @PathVariable("codeUid") String codeUid);

    @Operation(summary = "Update code")
    @PutMapping("/codes/{codeUid}")
    ResponseEntity<CodeDto> updateCode(
            @Parameter(description = "Code UID", example = "A1B2C3D4E5F6G7H8")
            @PathVariable("codeUid") String codeUid,
            @RequestBody CodeDto dto);

    @Operation(summary = "Delete code")
    @DeleteMapping("/codes/{codeUid}")
    ResponseEntity<Void> deleteCode(
            @Parameter(description = "Code UID", example = "A1B2C3D4E5F6G7H8")
            @PathVariable("codeUid") String codeUid);

    // ========== MASTER PASSWORD ==========
    @Operation(summary = "Set master password")
    @PostMapping("/master-password")
    ResponseEntity<MasterPasswordDto> setMasterPassword(@RequestBody MasterPasswordDto dto);

    @Operation(summary = "Get master password")
    @GetMapping("/master-password")
    ResponseEntity<MasterPasswordDto> getMasterPassword();

    // ========== AUDIT ==========
    @Operation(summary = "Log service event")
    @PostMapping("/audit/events")
    ResponseEntity<ServiceEventDto> logEvent(@RequestBody ServiceEventDto dto);

    @Operation(summary = "Get events by user")
    @GetMapping("/audit/events/user/{userId}")
    ResponseEntity<List<ServiceEventDto>> getEventsByUser(
            @Parameter(description = "User ID", example = "123")
            @PathVariable("userId") Long userId);

    @Operation(summary = "Get events by reference")
    @GetMapping("/audit/events/reference/{referenceId}")
    ResponseEntity<List<ServiceEventDto>> getEventsByReference(
            @Parameter(description = "Reference ID", example = "456")
            @PathVariable("referenceId") Long referenceId);

    // ========== PAYMENTS ==========
    @Operation(summary = "Create payment")
    @PostMapping("/payments")
    ResponseEntity<PaymentDto> createPayment(@RequestBody PaymentDto dto);

    @Operation(summary = "Get payments by card")
    @GetMapping("/payments/card/{cardId}")
    ResponseEntity<List<PaymentDto>> getPaymentsByCard(
            @Parameter(description = "Card ID", example = "789")
            @PathVariable("cardId") Long cardId);

    @Operation(summary = "Get payments by terminal")
    @GetMapping("/payments/terminal/{terminalId}")
    ResponseEntity<List<PaymentDto>> getPaymentsByTerminal(
            @Parameter(description = "Terminal ID", example = "101")
            @PathVariable("terminalId") Long terminalId);

    // ========== REFERENCE DATA ==========
    @Operation(summary = "Get all service event types")
    @GetMapping("/audit/event-types")
    ResponseEntity<List<ServiceEventTypeDto>> getAllEventTypes();

    @Operation(summary = "Get all payment types")
    @GetMapping("/payments/types")
    ResponseEntity<List<PaymentTypeDto>> getAllPaymentTypes();

    @Operation(summary = "Get all payment results")
    @GetMapping("/payments/results")
    ResponseEntity<List<PaymentResultDto>> getAllPaymentResults();

}