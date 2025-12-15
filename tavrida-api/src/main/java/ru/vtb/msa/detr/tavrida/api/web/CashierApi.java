package ru.vtb.msa.detr.tavrida.api.web;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.vtb.msa.detr.tavrida.api.model.cashier.*;
import ru.vtb.msa.detr.tavrida.api.model.terminal.TerminalActivationRequest;
import ru.vtb.msa.detr.tavrida.api.model.terminal.TerminalActivationResponse;
import ru.vtb.msa.detr.tavrida.api.model.terminal.TerminalDeactivationRequest;

@Tag(name = "Cashier API", description = "Операции кассира транспортной системы")
@RequestMapping("/v1/cashier")
public interface CashierApi {

    @Operation(summary = "Активация терминала (кассы) кассиром.")
    @PostMapping("/terminal/activate")
    ResponseEntity<TerminalActivationResponse> activateTerminal(@RequestBody TerminalActivationRequest request);

    @Operation(summary = "Деактивация терминала (кассы) кассиром.")
    @PostMapping("/terminal/deactivate")
    ResponseEntity<TerminalActivationResponse> deactivateTerminal(@RequestBody TerminalDeactivationRequest request);

    @Operation(summary = "Вход кассира в приложение")
    @PostMapping("/auth/login")
    ResponseEntity<CashierLoginResponse> cashierLogin(@RequestBody CashierLoginRequest request);

    @Operation(summary = "Выход кассира из приложение")
    @PostMapping("/auth/logout")
    ResponseEntity<CashierLogoutResponse> cashierLogout(CashierLogoutRequest request);

    @Operation(summary = "Активация пассажирской карты")
    @PostMapping("/card/init")
    ResponseEntity<CardInitResponse> initCard(@RequestBody CardInitRequest request);

    @Operation(summary = "Получение баланса карты")
    @PostMapping("/card/balance")
    ResponseEntity<CardBalanceResponse> getCardBalance(@RequestBody CardBalanceRequest request);

    @Operation(summary = "Покупка поездок на карту")
    @PostMapping("/card/purchase")
    ResponseEntity<CardPurchaseResponse> purchaseTrips(@RequestBody CardPurchaseRequest request);

}