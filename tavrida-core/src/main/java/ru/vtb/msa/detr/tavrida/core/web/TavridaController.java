package ru.vtb.msa.detr.tavrida.core.web;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import ru.vtb.msa.detr.tavrida.api.model.*;
import ru.vtb.msa.detr.tavrida.api.web.TavridaApi;
import ru.vtb.msa.detr.tavrida.core.service.*;

import java.util.List;
import java.util.UUID;

@RestController
public class TavridaController implements TavridaApi {

    private final BlackListService blackListService;
    private final CardService cardService;
    private final CardTypeService cardTypeService;
    private final CarrierService carrierService;
    private final TerminalService terminalService;
    private final TransportService transportService;
    private final UserRoleService userRoleService;
    private final UserService userService;

    public TavridaController(
            BlackListService blackListService,
            CardService cardService,
            CardTypeService cardTypeService,
            CarrierService carrierService,
            TerminalService terminalService,
            TransportService transportService,
            UserRoleService userRoleService,
            UserService userService) {
        this.blackListService = blackListService;
        this.cardService = cardService;
        this.cardTypeService = cardTypeService;
        this.carrierService = carrierService;
        this.terminalService = terminalService;
        this.transportService = transportService;
        this.userRoleService = userRoleService;
        this.userService = userService;
    }

    // ========== BLACKLIST ==========
    @Override
    public ResponseEntity<List<BlackListDto>> getAllBlackListEntries() {
        return ResponseEntity.ok(blackListService.getAllBlackListEntries());
    }

    @Override
    public ResponseEntity<BlackListDto> getBlackListEntryById(UUID id) {
        return ResponseEntity.ok(blackListService.getBlackListEntryById(id));
    }

    @Override
    public ResponseEntity<BlackListDto> addCardToBlackList(BlackListDto dto) {
        return ResponseEntity.ok(blackListService.addCardToBlackList(dto));
    }

    @Override
    public ResponseEntity<Void> removeCardFromBlackList(UUID id) {
        blackListService.removeCardFromBlackList(id);
        return ResponseEntity.noContent().build();
    }

    @Override
    public ResponseEntity<Boolean> isCardBlocked(UUID cardGuid) {
        return ResponseEntity.ok(blackListService.isCardBlocked(cardGuid));
    }

    // ========== CARDS ==========
    @Override
    public ResponseEntity<List<CardDto>> getAllCards() {
        return ResponseEntity.ok(cardService.getAllCards());
    }

    @Override
    public ResponseEntity<CardDto> getCardById(Long id) {
        return ResponseEntity.ok(cardService.getCardById(id));
    }

    @Override
    public ResponseEntity<CardDto> getCardByGuid(UUID guid) {
        return ResponseEntity.ok(cardService.getCardByGuid(guid));
    }

    @Override
    public ResponseEntity<CardDto> createCard(CardDto dto) {
        return ResponseEntity.ok(cardService.createCard(dto));
    }

    @Override
    public ResponseEntity<CardDto> updateCard(Long id, CardDto dto) {
        return ResponseEntity.ok(cardService.updateCard(id, dto));
    }

    @Override
    public ResponseEntity<Void> deleteCard(Long id) {
        cardService.deleteCard(id);
        return ResponseEntity.noContent().build();
    }

    // ========== CARD TYPES ==========
    @Override
    public ResponseEntity<List<CardTypeDto>> getAllCardTypes() {
        return ResponseEntity.ok(cardTypeService.getAllCardTypes());
    }

    @Override
    public ResponseEntity<CardTypeDto> getCardTypeById(Integer id) {
        return ResponseEntity.ok(cardTypeService.getCardTypeById(id));
    }

    @Override
    public ResponseEntity<CardTypeDto> createCardType(CardTypeDto dto) {
        return ResponseEntity.ok(cardTypeService.createCardType(dto));
    }

    @Override
    public ResponseEntity<CardTypeDto> updateCardType(Integer id, CardTypeDto dto) {
        return ResponseEntity.ok(cardTypeService.updateCardType(id, dto));
    }

    @Override
    public ResponseEntity<Void> deleteCardType(Integer id) {
        cardTypeService.deleteCardType(id);
        return ResponseEntity.noContent().build();
    }

    // ========== CARRIERS ==========
    @Override
    public ResponseEntity<List<CarrierDto>> getAllCarriers() {
        return ResponseEntity.ok(carrierService.getAllCarriers());
    }

    @Override
    public ResponseEntity<CarrierDto> getCarrierById(Long id) {
        return ResponseEntity.ok(carrierService.getCarrierById(id));
    }

    @Override
    public ResponseEntity<CarrierDto> createCarrier(CarrierDto dto) {
        return ResponseEntity.ok(carrierService.createCarrier(dto));
    }

    @Override
    public ResponseEntity<CarrierDto> updateCarrier(Long id, CarrierDto dto) {
        return ResponseEntity.ok(carrierService.updateCarrier(id, dto));
    }

    @Override
    public ResponseEntity<Void> deleteCarrier(Long id) {
        carrierService.deleteCarrier(id);
        return ResponseEntity.noContent().build();
    }

    // ========== TERMINALS ==========
    @Override
    public ResponseEntity<List<TerminalDto>> getAllTerminals() {
        return ResponseEntity.ok(terminalService.getAllTerminals());
    }

    @Override
    public ResponseEntity<TerminalDto> getTerminalById(Long id) {
        return ResponseEntity.ok(terminalService.getTerminalById(id));
    }

    @Override
    public ResponseEntity<TerminalDto> createTerminal(TerminalDto dto) {
        return ResponseEntity.ok(terminalService.createTerminal(dto));
    }

    @Override
    public ResponseEntity<TerminalDto> updateTerminal(Long id, TerminalDto dto) {
        return ResponseEntity.ok(terminalService.updateTerminal(id, dto));
    }

    @Override
    public ResponseEntity<Void> deleteTerminal(Long id) {
        terminalService.deleteTerminal(id);
        return ResponseEntity.noContent().build();
    }

    // ========== TRANSPORTS ==========
    @Override
    public ResponseEntity<List<TransportDto>> getAllTransports() {
        return ResponseEntity.ok(transportService.getAllTransports());
    }

    @Override
    public ResponseEntity<TransportDto> getTransportById(Long id) {
        return ResponseEntity.ok(transportService.getTransportById(id));
    }

    @Override
    public ResponseEntity<TransportDto> createTransport(TransportDto dto) {
        return ResponseEntity.ok(transportService.createTransport(dto));
    }

    @Override
    public ResponseEntity<TransportDto> updateTransport(Long id, TransportDto dto) {
        return ResponseEntity.ok(transportService.updateTransport(id, dto));
    }

    @Override
    public ResponseEntity<Void> deleteTransport(Long id) {
        transportService.deleteTransport(id);
        return ResponseEntity.noContent().build();
    }

    // ========== USER ROLES ==========
    @Override
    public ResponseEntity<List<UserRoleDto>> getAllUserRoles() {
        return ResponseEntity.ok(userRoleService.getAllRoles());
    }

    @Override
    public ResponseEntity<UserRoleDto> getUserRoleById(Integer id) {
        return ResponseEntity.ok(userRoleService.getRoleById(id));
    }

    @Override
    public ResponseEntity<UserRoleDto> createUserRole(UserRoleDto dto) {
        return ResponseEntity.ok(userRoleService.createRole(dto));
    }

    @Override
    public ResponseEntity<UserRoleDto> updateUserRole(Integer id, UserRoleDto dto) {
        return ResponseEntity.ok(userRoleService.updateRole(id, dto));
    }

    @Override
    public ResponseEntity<Void> deleteUserRole(Integer id) {
        userRoleService.deleteRole(id);
        return ResponseEntity.noContent().build();
    }

    // ========== USERS ==========
    @Override
    public ResponseEntity<List<UserDto>> getAllUsers() {
        return ResponseEntity.ok(userService.getAllUsers());
    }

    @Override
    public ResponseEntity<UserDto> getUserById(Long id) {
        return ResponseEntity.ok(userService.getUserById(id));
    }

    @Override
    public ResponseEntity<UserDto> createUser(UserDto dto) {
        return ResponseEntity.ok(userService.createUser(dto));
    }

    @Override
    public ResponseEntity<UserDto> updateUser(Long id, UserDto dto) {
        return ResponseEntity.ok(userService.updateUser(id, dto));
    }

    @Override
    public ResponseEntity<Void> deleteUser(Long id) {
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }
}