package ru.vtb.msa.detr.tavrida.core.web;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import ru.vtb.msa.detr.tavrida.api.model.terminal.*;
import ru.vtb.msa.detr.tavrida.api.web.TerminalOperationApi;
import ru.vtb.msa.detr.tavrida.core.service.TerminalOperationService;

import java.util.List;
import java.util.Set;
import java.util.UUID;

@RestController
public class TerminalOperationController implements TerminalOperationApi {

    private final TerminalOperationService terminalOperationService;

    public TerminalOperationController(TerminalOperationService terminalOperationService) {
        this.terminalOperationService = terminalOperationService;
    }

    @Override
    public ResponseEntity<DriverSessionResponse> startDriverSession(@RequestBody DriverSessionStartRequest request) {
        DriverSessionResponse response = terminalOperationService.startDriverSession(request);
        return ResponseEntity.ok(response);
    }

    @Override
    public ResponseEntity<TerminalActivationResponse> activateTerminal(@RequestBody TerminalActivationRequest request) {
        TerminalActivationResponse response = terminalOperationService.activateTerminal(request);
        return ResponseEntity.ok(response);
    }

    @Override
    public ResponseEntity<TerminalActivationResponse> deactivateTerminal(@RequestBody TerminalDeactivationRequest request) {
        TerminalActivationResponse response = terminalOperationService.deactivateTerminal(request);
        return ResponseEntity.ok(response);
    }

    @Override
    public ResponseEntity<Boolean> deductTrip(TerminalDeductRequest request) {
        boolean success = terminalOperationService.deductTrip(request);
        return ResponseEntity.ok(success);
    }

    @Override
    public ResponseEntity<List<UUID>> deductsTrip(List<TerminalDeductRequest> request) {
        List<UUID> not_success_list = terminalOperationService.deductsTrip(request);
        return ResponseEntity.ok(not_success_list);
    }

    @Override
    public ResponseEntity<Boolean> isCardBlocked(UUID cardGuid) {
        boolean blocked = terminalOperationService.isCardBlocked(cardGuid);
        return ResponseEntity.ok(blocked);
    }

    @Override
    public ResponseEntity<Set<UUID>> getFullBlackList() {
        Set<UUID> blackList = terminalOperationService.getFullBlackList();
        return ResponseEntity.ok(blackList);
    }
}