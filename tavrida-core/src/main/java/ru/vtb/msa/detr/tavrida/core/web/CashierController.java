package ru.vtb.msa.detr.tavrida.core.web;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import ru.vtb.msa.detr.tavrida.api.model.cashier.*;
import ru.vtb.msa.detr.tavrida.api.model.terminal.CashierTerminalActivationRequest;
import ru.vtb.msa.detr.tavrida.api.model.terminal.TerminalActivationRequest;
import ru.vtb.msa.detr.tavrida.api.model.terminal.TerminalActivationResponse;
import ru.vtb.msa.detr.tavrida.api.model.terminal.TerminalDeactivationRequest;
import ru.vtb.msa.detr.tavrida.api.web.CashierApi;
import ru.vtb.msa.detr.tavrida.core.service.CashierService;

@Controller
public class CashierController implements CashierApi {

    private final CashierService cashierService;

    public CashierController(CashierService cashierService) {
        this.cashierService = cashierService;
    }

    @Override
    public ResponseEntity<TerminalActivationResponse> activateTerminal(@RequestBody CashierTerminalActivationRequest request) {
        TerminalActivationResponse response = cashierService.activateTerminal(request);
        return ResponseEntity.ok(response);
    }

    @Override
    public ResponseEntity<TerminalActivationResponse> deactivateTerminal(@RequestBody TerminalDeactivationRequest request) {
        TerminalActivationResponse response = cashierService.deactivateTerminal(request);
        return ResponseEntity.ok(response);
    }

    @Override
    public ResponseEntity<CashierLoginResponse> cashierLogin(CashierLoginRequest request) {
        return ResponseEntity.ok(cashierService.cashierLogin(request));
    }

    @Override
    public ResponseEntity<CashierLogoutResponse> cashierLogout(CashierLogoutRequest request) {
        return ResponseEntity.ok(cashierService.cashierLogout(request));
    }

    @Override
    public ResponseEntity<CardInitResponse> initCard(CardInitRequest request) {
        return ResponseEntity.ok(cashierService.initCard(request));
    }

    @Override
    public ResponseEntity<CardBalanceResponse> getCardBalance(CardBalanceRequest request) {
        return ResponseEntity.ok(cashierService.getCardBalance(request));
    }

    @Override
    public ResponseEntity<CardPurchaseResponse> purchaseTrips(CardPurchaseRequest request) {
        return ResponseEntity.ok(cashierService.purchaseTrips(request));
    }
}