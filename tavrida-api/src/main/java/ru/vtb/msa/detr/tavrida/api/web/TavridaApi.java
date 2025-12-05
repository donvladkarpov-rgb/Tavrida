package ru.vtb.msa.detr.tavrida.api.web;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.vtb.msa.detr.tavrida.api.model.*;

import java.util.List;
import java.util.UUID;

@Tag(name = "Tavrida Transport System", description = "Unified API for Tavrida transport card system")
@RequestMapping("/v1/tavrida")
public interface TavridaApi {

    // ========== BLACKLIST ==========
    @Operation(summary = "Get all blacklist entries")
    @GetMapping("/blacklist")
    ResponseEntity<List<BlackListDto>> getAllBlackListEntries();

    @Operation(summary = "Get blacklist entry by ID")
    @GetMapping("/blacklist/{id}")
    ResponseEntity<BlackListDto> getBlackListEntryById(
            @Parameter(description = "Blacklist entry ID", example = "550e8400-e29b-41d4-a716-446655440000")
            @PathVariable("id") UUID id);

    @Operation(summary = "Add card to blacklist")
    @PostMapping("/blacklist")
    ResponseEntity<BlackListDto> addCardToBlackList(@RequestBody BlackListDto dto);

    @Operation(summary = "Remove card from blacklist")
    @DeleteMapping("/blacklist/{id}")
    ResponseEntity<Void> removeCardFromBlackList(
            @Parameter(description = "Blacklist entry ID", example = "550e8400-e29b-41d4-a716-446655440000")
            @PathVariable("id") UUID id);

    @Operation(summary = "Check if card is blocked")
    @GetMapping("/blacklist/check/{cardGuid}")
    ResponseEntity<Boolean> isCardBlocked(
            @Parameter(description = "Card GUID", example = "550e8400-e29b-41d4-a716-446655440000")
            @PathVariable("cardGuid") UUID cardGuid);

    // ========== CARDS ==========
    @Operation(summary = "Get all cards")
    @GetMapping("/cards")
    ResponseEntity<List<CardDto>> getAllCards();

    @Operation(summary = "Get card by ID")
    @GetMapping("/cards/{id}")
    ResponseEntity<CardDto> getCardById(
            @Parameter(description = "Card ID", example = "1")
            @PathVariable("id") Long id);

    @Operation(summary = "Get card by GUID")
    @GetMapping("/cards/guid/{guid}")
    ResponseEntity<CardDto> getCardByGuid(
            @Parameter(description = "Card GUID", example = "550e8400-e29b-41d4-a716-446655440000")
            @PathVariable("guid") UUID guid);

    @Operation(summary = "Create new card")
    @PostMapping("/cards")
    ResponseEntity<CardDto> createCard(@RequestBody CardDto dto);

    @Operation(summary = "Update card")
    @PutMapping("/cards/{id}")
    ResponseEntity<CardDto> updateCard(
            @Parameter(description = "Card ID", example = "1")
            @PathVariable("id") Long id,
            @RequestBody CardDto dto);

    @Operation(summary = "Delete card")
    @DeleteMapping("/cards/{id}")
    ResponseEntity<Void> deleteCard(
            @Parameter(description = "Card ID", example = "1")
            @PathVariable("id") Long id);

    // ========== CARD TYPES ==========
    @Operation(summary = "Get all card types")
    @GetMapping("/card-types")
    ResponseEntity<List<CardTypeDto>> getAllCardTypes();

    @Operation(summary = "Get card type by ID")
    @GetMapping("/card-types/{id}")
    ResponseEntity<CardTypeDto> getCardTypeById(
            @Parameter(description = "Card type ID", example = "1")
            @PathVariable("id") Integer id);

    @Operation(summary = "Create card type")
    @PostMapping("/card-types")
    ResponseEntity<CardTypeDto> createCardType(@RequestBody CardTypeDto dto);

    @Operation(summary = "Update card type")
    @PutMapping("/card-types/{id}")
    ResponseEntity<CardTypeDto> updateCardType(
            @Parameter(description = "Card type ID", example = "1")
            @PathVariable("id") Integer id,
            @RequestBody CardTypeDto dto);

    @Operation(summary = "Delete card type")
    @DeleteMapping("/card-types/{id}")
    ResponseEntity<Void> deleteCardType(
            @Parameter(description = "Card type ID", example = "1")
            @PathVariable("id") Integer id);

    // ========== CARRIERS ==========
    @Operation(summary = "Get all carriers")
    @GetMapping("/carriers")
    ResponseEntity<List<CarrierDto>> getAllCarriers();

    @Operation(summary = "Get carrier by ID")
    @GetMapping("/carriers/{id}")
    ResponseEntity<CarrierDto> getCarrierById(
            @Parameter(description = "Carrier ID", example = "1")
            @PathVariable("id") Long id);

    @Operation(summary = "Create carrier")
    @PostMapping("/carriers")
    ResponseEntity<CarrierDto> createCarrier(@RequestBody CarrierDto dto);

    @Operation(summary = "Update carrier")
    @PutMapping("/carriers/{id}")
    ResponseEntity<CarrierDto> updateCarrier(
            @Parameter(description = "Carrier ID", example = "1")
            @PathVariable("id") Long id,
            @RequestBody CarrierDto dto);

    @Operation(summary = "Delete carrier")
    @DeleteMapping("/carriers/{id}")
    ResponseEntity<Void> deleteCarrier(
            @Parameter(description = "Carrier ID", example = "1")
            @PathVariable("id") Long id);

    // ========== TERMINALS ==========
    @Operation(summary = "Get all terminals")
    @GetMapping("/terminals")
    ResponseEntity<List<TerminalDto>> getAllTerminals();

    @Operation(summary = "Get terminal by ID")
    @GetMapping("/terminals/{id}")
    ResponseEntity<TerminalDto> getTerminalById(
            @Parameter(description = "Terminal ID", example = "1")
            @PathVariable("id") Long id);

    @Operation(summary = "Create terminal")
    @PostMapping("/terminals")
    ResponseEntity<TerminalDto> createTerminal(@RequestBody TerminalDto dto);

    @Operation(summary = "Update terminal")
    @PutMapping("/terminals/{id}")
    ResponseEntity<TerminalDto> updateTerminal(
            @Parameter(description = "Terminal ID", example = "1")
            @PathVariable("id") Long id,
            @RequestBody TerminalDto dto);

    @Operation(summary = "Delete terminal")
    @DeleteMapping("/terminals/{id}")
    ResponseEntity<Void> deleteTerminal(
            @Parameter(description = "Terminal ID", example = "1")
            @PathVariable("id") Long id);

    // ========== TRANSPORTS ==========
    @Operation(summary = "Get all transports")
    @GetMapping("/transports")
    ResponseEntity<List<TransportDto>> getAllTransports();

    @Operation(summary = "Get transport by ID")
    @GetMapping("/transports/{id}")
    ResponseEntity<TransportDto> getTransportById(
            @Parameter(description = "Transport ID", example = "1")
            @PathVariable("id") Long id);

    @Operation(summary = "Create transport")
    @PostMapping("/transports")
    ResponseEntity<TransportDto> createTransport(@RequestBody TransportDto dto);

    @Operation(summary = "Update transport")
    @PutMapping("/transports/{id}")
    ResponseEntity<TransportDto> updateTransport(
            @Parameter(description = "Transport ID", example = "1")
            @PathVariable("id") Long id,
            @RequestBody TransportDto dto);

    @Operation(summary = "Delete transport")
    @DeleteMapping("/transports/{id}")
    ResponseEntity<Void> deleteTransport(
            @Parameter(description = "Transport ID", example = "1")
            @PathVariable("id") Long id);

    // ========== USER ROLES ==========
    @Operation(summary = "Get all user roles")
    @GetMapping("/user-roles")
    ResponseEntity<List<UserRoleDto>> getAllUserRoles();

    @Operation(summary = "Get user role by ID")
    @GetMapping("/user-roles/{id}")
    ResponseEntity<UserRoleDto> getUserRoleById(
            @Parameter(description = "User role ID", example = "1")
            @PathVariable("id") Integer id);

    @Operation(summary = "Create user role")
    @PostMapping("/user-roles")
    ResponseEntity<UserRoleDto> createUserRole(@RequestBody UserRoleDto dto);

    @Operation(summary = "Update user role")
    @PutMapping("/user-roles/{id}")
    ResponseEntity<UserRoleDto> updateUserRole(
            @Parameter(description = "User role ID", example = "1")
            @PathVariable("id") Integer id,
            @RequestBody UserRoleDto dto);

    @Operation(summary = "Delete user role")
    @DeleteMapping("/user-roles/{id}")
    ResponseEntity<Void> deleteUserRole(
            @Parameter(description = "User role ID", example = "1")
            @PathVariable("id") Integer id);

    // ========== USERS ==========
    @Operation(summary = "Get all users")
    @GetMapping("/users")
    ResponseEntity<List<UserDto>> getAllUsers();

    @Operation(summary = "Get user by ID")
    @GetMapping("/users/{id}")
    ResponseEntity<UserDto> getUserById(
            @Parameter(description = "User ID", example = "1")
            @PathVariable("id") Long id);

    @Operation(summary = "Create user")
    @PostMapping("/users")
    ResponseEntity<UserDto> createUser(@RequestBody UserDto dto);

    @Operation(summary = "Update user")
    @PutMapping("/users/{id}")
    ResponseEntity<UserDto> updateUser(
            @Parameter(description = "User ID", example = "1")
            @PathVariable("id") Long id,
            @RequestBody UserDto dto);

    @Operation(summary = "Delete user")
    @DeleteMapping("/users/{id}")
    ResponseEntity<Void> deleteUser(
            @Parameter(description = "User ID", example = "1")
            @PathVariable("id") Long id);
}