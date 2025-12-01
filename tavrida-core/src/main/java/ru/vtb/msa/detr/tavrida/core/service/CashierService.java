package ru.vtb.msa.detr.tavrida.core.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.vtb.msa.detr.tavrida.api.model.PaymentDto;
import ru.vtb.msa.detr.tavrida.api.model.PaymentResultDto;
import ru.vtb.msa.detr.tavrida.api.model.PaymentTypeDto;
import ru.vtb.msa.detr.tavrida.api.model.UserSessionDto;
import ru.vtb.msa.detr.tavrida.api.model.cashier.*;
import ru.vtb.msa.detr.tavrida.core.exception.AuthenticationTavridaException;
import ru.vtb.msa.detr.tavrida.core.exception.EntityNotFoundException;
import ru.vtb.msa.detr.tavrida.core.model.*;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.UUID;

@Service
public class CashierService {

    private final SessionService sessionService;
    private final CardService cardService;
    private final UserService userService;
    private final PaymentService paymentService;
    private final TerminalService terminalService;
    private final CardTypeService cardTypeService;

    public CashierService(
            SessionService sessionService,
            CardService cardService,
            UserService userService,
            PaymentService paymentService,
            TerminalService terminalService,
            CardTypeService cardTypeService) {
        this.sessionService = sessionService;
        this.cardService = cardService;
        this.userService = userService;
        this.paymentService = paymentService;
        this.terminalService = terminalService;
        this.cardTypeService = cardTypeService;
    }

    @Transactional
    public CashierLoginResponse cashierLogin(CashierLoginRequest request) {
        // 1. Находим карту кассира
        Card cashierCard = cardService.getCardEntityByGuid(request.getCardUuid());

        // 2. Проверяем, что это карта кассира (CARD_TYPE_ID = 4)
        if (cashierCard.getCardType().getCardTypeId() != 4) {
            throw new AuthenticationTavridaException("Карта не принадлежит кассиру");
        }

        // 3. Проверяем пароль и роль
        User user = cashierCard.getUser();
        if (!user.getUserPasswordHash().equals(request.getHashPassword())) {
            throw new AuthenticationTavridaException("Неверный пароль");
        }
        if (!user.getUserRole().getUserRoleId().equals(3)) {
            throw new AuthenticationTavridaException("Роль у пользователя должна быть - кассир");
        }

        // 4. Проверяем терминал
        Terminal terminal = terminalService.getTerminalEntityByTerminalGuid(request.getTerminalGuid())
                .orElseThrow(() -> new EntityNotFoundException("Terminal not found: " + request.getTerminalGuid()));

        // 5. Создаём сессию
        UserSession session = new UserSession();
        session.setSessionId(UUID.randomUUID());
        session.setUserId(user.getUserId());
        session.setTerminal(terminal);
        session.setExpirationTime(LocalDateTime.now().plusHours(8));
        UserSession savedSession = sessionService.save(session);
        return new CashierLoginResponse(savedSession.getSessionId());
    }

    @Transactional
    public CardInitResponse initCard(CardInitRequest request) {
        sessionService.getSession(request.getSessionId()); // валидация

        Card newCard = new Card();
        newCard.setCardGuid(UUID.randomUUID());
        newCard.setCardType(cardTypeService.getCardTypeEntityById(4)); // Пассажирская
        newCard.setMaximumUniqueCount(0);
        newCard.setUniqueTravelCount(0);
        newCard.setAvailableTravelCount(0);
        newCard.setExpirationDate(Instant.now().plus(365 * 5, ChronoUnit.DAYS));

        Card savedCard = cardService.save(newCard);
        return new CardInitResponse(
                savedCard.getCardGuid(),
                LocalDateTime.ofInstant(savedCard.getExpirationDate(), ZoneId.systemDefault())
        );
    }

    public CardBalanceResponse getCardBalance(CardBalanceRequest request) {
        sessionService.getSession(request.getSessionId()); // валидация

        Card card = cardService.getCardEntityByGuid(request.getCardUuid());
        return new CardBalanceResponse(card.getAvailableTravelCount());
    }

    @Transactional
    public CardPurchaseResponse purchaseTrips(CardPurchaseRequest request) {
        UserSessionDto session = sessionService.getSession(request.getSessionId());

        Card card = cardService.getCardEntityByGuid(request.getCardUuid());
        int newBalance = card.getAvailableTravelCount() + request.getTripsCount();
        card.setAvailableTravelCount(newBalance);
        Card updatedCard = cardService.save(card);

        PaymentDto payment = new PaymentDto();
        payment.setCardId(card.getCardId());
        payment.setPaymentTime(LocalDateTime.now());
        payment.setBalanceBefore(BigDecimal.valueOf(card.getAvailableTravelCount() - request.getTripsCount()));
        payment.setBalanceAfter(BigDecimal.valueOf(newBalance));
        payment.setPaymentType(new PaymentTypeDto(1)); // Пополнение
        payment.setPaymentResult(new PaymentResultDto(11)); // Успех
        payment.setTerminalId(session.getTerminalId());

        PaymentDto savedPayment = paymentService.createPayment(payment);
        return new CardPurchaseResponse(
                updatedCard.getCardGuid(),
                updatedCard.getAvailableTravelCount(),
                savedPayment.getPaymentId()
        );
    }
}