package ru.vtb.msa.detr.tavrida.core.web;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import ru.vtb.msa.detr.tavrida.api.model.cashier.*;
import ru.vtb.msa.detr.tavrida.api.web.CashierApi;
import ru.vtb.msa.detr.tavrida.core.service.CashierService;

@Controller
public class CashierController implements CashierApi {

    private final CashierService cashierService;

    public CashierController(CashierService cashierService) {
        this.cashierService = cashierService;
    }

    @Override
    public ResponseEntity<CashierLoginResponse> cashierLogin(CashierLoginRequest request) {
        return ResponseEntity.ok(cashierService.cashierLogin(request));
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