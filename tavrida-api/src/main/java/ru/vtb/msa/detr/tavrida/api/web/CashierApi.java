package ru.vtb.msa.detr.tavrida.api.web;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import ru.vtb.msa.detr.tavrida.api.model.cashier.*;

@Tag(name = "Cashier API", description = "Операции кассира транспортной системы")
public interface CashierApi {

    @Operation(summary = "Вход кассира в приложение")
    @PostMapping("/cashier/auth/login")
    ResponseEntity<CashierLoginResponse> cashierLogin(@RequestBody CashierLoginRequest request);

    @Operation(summary = "Активация пассажирской карты")
    @PostMapping("/cashier/card/init")
    ResponseEntity<CardInitResponse> initCard(@RequestBody CardInitRequest request);

    @Operation(summary = "Получение баланса карты")
    @PostMapping("/cashier/card/balance")
    ResponseEntity<CardBalanceResponse> getCardBalance(@RequestBody CardBalanceRequest request);

    @Operation(summary = "Покупка поездок на карту")
    @PostMapping("/cashier/card/purchase")
    ResponseEntity<CardPurchaseResponse> purchaseTrips(@RequestBody CardPurchaseRequest request);

}