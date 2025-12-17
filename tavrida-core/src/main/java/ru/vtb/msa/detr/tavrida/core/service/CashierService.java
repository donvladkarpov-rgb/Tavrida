package ru.vtb.msa.detr.tavrida.core.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.vtb.msa.detr.tavrida.api.model.PaymentDto;
import ru.vtb.msa.detr.tavrida.api.model.PaymentResultDto;
import ru.vtb.msa.detr.tavrida.api.model.PaymentTypeDto;
import ru.vtb.msa.detr.tavrida.api.model.UserSessionDto;
import ru.vtb.msa.detr.tavrida.api.model.cashier.*;
import ru.vtb.msa.detr.tavrida.api.model.terminal.*;
import ru.vtb.msa.detr.tavrida.core.config.TavridaConstants;
import ru.vtb.msa.detr.tavrida.core.exception.AuthenticationTavridaException;
import ru.vtb.msa.detr.tavrida.core.exception.EntityNotFoundException;
import ru.vtb.msa.detr.tavrida.core.model.*;
import ru.vtb.msa.detr.tavrida.core.model.mapper.TavridaMapper;
import ru.vtb.msa.detr.tavrida.core.repo.*;
import ru.vtb.msa.detr.tavrida.core.util.TavridaUtils;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.Objects;
import java.util.UUID;

@Service
public class CashierService {

    private final SessionService sessionService;
    private final CardService cardService;
    private final UserService userService;
    private final PaymentService paymentService;
    private final TerminalService terminalService;
    private final CardTypeService cardTypeService;
    private final TavridaConstants tavridaConstants;
    private final TransportRepository transportRepository;
    private final TerminalRepository terminalRepository;
    private final CardRepository cardRepository;
    private final UserRepository userRepository;
    private final CarrierRepository carrierRepository;
    private final UserSessionRepository userSessionRepository;
    private final TavridaUtils util;

    public CashierService(
            SessionService sessionService,
            CardService cardService,
            UserService userService,
            PaymentService paymentService,
            TerminalService terminalService,
            CardTypeService cardTypeService,
            TavridaConstants tavridaConstants,
            TransportRepository transportRepository,
            TerminalRepository terminalRepository,
            CardRepository cardRepository,
            UserRepository userRepository,
            CarrierRepository carrierRepository,
            UserSessionRepository userSessionRepository,
            TavridaUtils util) {
        this.sessionService = sessionService;
        this.cardService = cardService;
        this.userService = userService;
        this.paymentService = paymentService;
        this.terminalService = terminalService;
        this.cardTypeService = cardTypeService;
        this.tavridaConstants = tavridaConstants;
        this.transportRepository = transportRepository;
        this.terminalRepository = terminalRepository;
        this.cardRepository = cardRepository;
        this.userRepository = userRepository;
        this.carrierRepository = carrierRepository;
        this.userSessionRepository = userSessionRepository;
        this.util = util;
    }

    @Transactional
    public TerminalActivationResponse activateTerminal(CashierTerminalActivationRequest request) {
        if (request.getTerminalGuid() == null) {
            request.setTerminalGuid(UUID.randomUUID());
            request.setTerminalNumber(util.generateCode());
        }
        if (request.getCardGuid() == null) {
            return new TerminalActivationResponse(false, "cardGuid не может быть null");
        }
        if (request.getCarrierId() == null) {
            return new TerminalActivationResponse(false, "carrierId не может быть null");
        }

        Carrier carrier = carrierRepository.findById(request.getCarrierId()).orElse(null);

        // 2. Проверка терминала
        Terminal terminal = terminalRepository.findByTerminalGuid(request.getTerminalGuid())
                .orElse(null);
        if (terminal != null) {
            if (terminal.getTransport() != null) {
                if (!Objects.equals(terminal.getCarrier().getCarrierId(), carrier.getCarrierId())) {
                    return new TerminalActivationResponse(false, "Терминал с таким GUID уже привязан к другому перевозчику!");
                } else {
                    // 6. Если уже привязан к тому же — ничего не делаем (идемпотентность)
                    return new TerminalActivationResponse(true,
                            "Терминал уже привязан к перевозчику ID " + carrier.getCarrierId(), TavridaMapper.toTerminalDto(terminal));
                }
            }
        } else {
            terminal = new Terminal();
            terminal.setTerminalGuid(request.getTerminalGuid());
            terminal.setTerminalNumber(request.getTerminalNumber());
            terminal.setTerminalSerialNumber(request.getTerminalNumber());
        }
        terminal.setTransport(null);
        terminal.setCarrier(carrier);

        // 3. Проверка карты активации
        Card card = cardRepository.findByCardGuid(request.getCardGuid())
                .orElse(null);

        if (card == null) {
            return new TerminalActivationResponse(false, "Карта с таким GUID не найдена");
        }

        // Проверка типа карты: должна быть "Карта активации терминала"
        if (!card.getCardType().getCardTypeId().equals(tavridaConstants.getCardTypeTerminalActivation())) {
            return new TerminalActivationResponse(false, "Карта не является картой активации терминала");
        }

        // Проверка: привязана ли карта к пользователю?
        if (card.getUser().getUserId() == null) {
            return new TerminalActivationResponse(false, "Карта не привязана ни к одному пользователю");
        }

        // 4. Проверка роли пользователя
        User user = userRepository.findById(card.getUserId())
                .orElse(null);

        if (user == null) {
            return new TerminalActivationResponse(false, "Пользователь, которому принадлежит карта, не найден");
        }

        Integer userRoleId = user.getUserRoleId();
        if (!userRoleId.equals(tavridaConstants.getUserRoleCarrierAdmin()) &&
                !userRoleId.equals(tavridaConstants.getUserRoleOperatorFundsAdmin()) &&
                !userRoleId.equals(tavridaConstants.getUserRoleCashier())
        ) {
            return new TerminalActivationResponse(false,
                    "Карта принадлежит пользователю без прав на активацию терминала. Требуется администратор перевозчика или оператора или кассир.");
        }

        // 7. Привязываем
        terminalRepository.save(terminal);

        return new TerminalActivationResponse(true,
                "Терминал успешно привязан к перевозчику ID " + carrier.getCarrierId(),
                TavridaMapper.toTerminalDto(terminal));
    }


    @Transactional
    public CashierLoginResponse cashierLogin(CashierLoginRequest request) {
        // 1. Находим карту кассира
        Card cashierCard = cardService.getCardEntityByGuid(request.getCardUuid());

        // 2. Проверяем, что это карта кассира (CARD_TYPE_ID = 2)
        if (!Objects.equals(cashierCard.getCardType().getCardTypeId(), tavridaConstants.getCardTypeCashier())) {
            throw new AuthenticationTavridaException("Карта не принадлежит кассиру");
        }

        // 3. Проверяем пароль и роль
        User user = cashierCard.getUser();
        if (!user.getUserPasswordHash().equals(request.getHashPassword())) {
            throw new AuthenticationTavridaException("Неверный пароль");
        }
        if (!user.getUserRole().getUserRoleId().equals(tavridaConstants.getUserRoleCashier())) {
            throw new AuthenticationTavridaException("Роль у пользователя должна быть - кассир");
        }

        // 4. Проверяем терминал
        Terminal terminal = terminalService.getTerminalEntityByTerminalGuid(request.getTerminalGuid())
                .orElseThrow(() -> new EntityNotFoundException("Terminal not found: " + request.getTerminalGuid()));

        // 5. Создаём сессию
        UserSession session = new UserSession();
        session.setSessionId(UUID.randomUUID());
        session.setUser(user);
        session.setTerminal(terminal);
        session.setStartedAt(Instant.now());
        session.setClosedAt(null);
        session.setExpirationTime(Instant.now().plus(31, ChronoUnit.DAYS));
        UserSession savedSession = sessionService.save(session);
        return new CashierLoginResponse(savedSession.getSessionId());
    }

    @Transactional
    public CardInitResponse initCard(CardInitRequest request) {
        sessionService.getSession(request.getSessionId()); // валидация

        Card newCard = new Card();
        newCard.setCardGuid(UUID.randomUUID());
        newCard.setCardType(cardTypeService.getCardTypeEntityById(tavridaConstants.getCardTypePassenger())); // Пассажирская
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

    @Transactional
    public CardBalanceResponse getCardBalance(CardBalanceRequest request) {
        sessionService.getSession(request.getSessionId()); // валидация

        Card card = cardService.getCardEntityByGuid(request.getCardUuid());
        card.setAvailableTravelCount(Math.min(card.getAvailableTravelCount(), request.getCurrentTripsCount()));
        cardService.save(card);
        return new CardBalanceResponse(card.getAvailableTravelCount());
    }

    @Transactional
    public CardPurchaseResponse purchaseTrips(CardPurchaseRequest request) {
        UserSessionDto session = sessionService.getSession(request.getSessionId());

        Card card = cardService.getCardEntityByGuid(request.getCardUuid());
        int newBalance = Math.min(card.getAvailableTravelCount(), request.getCurrentTripsCount()) + request.getTripsCount();
        card.setAvailableTravelCount(newBalance);
        Card updatedCard = cardService.save(card);

        PaymentDto payment = new PaymentDto();
        payment.setCardId(card.getCardId());
        payment.setPaymentTime(LocalDateTime.now());
        payment.setBalanceBefore(BigDecimal.valueOf(card.getAvailableTravelCount() - request.getTripsCount()));
        payment.setBalanceAfter(BigDecimal.valueOf(newBalance));
        payment.setPaymentType(new PaymentTypeDto(tavridaConstants.getPaymentTypeReplenishment())); // Пополнение
        payment.setPaymentResult(new PaymentResultDto(tavridaConstants.getPaymentResultSuccess())); // Успех
        payment.setTerminalId(session.getTerminalId());

        PaymentDto savedPayment = paymentService.createPayment(payment);
        return new CardPurchaseResponse(
                updatedCard.getCardGuid(),
                updatedCard.getAvailableTravelCount(),
                savedPayment.getPaymentId()
        );
    }

    public TerminalActivationResponse deactivateTerminal(TerminalDeactivationRequest request) {
        // Валидация входных данных
        if (request.getTerminalGuid() == null) {
            return new TerminalActivationResponse(false, "terminalGuid не может быть null");
        }
        if (request.getCardGuid() == null) {
            return new TerminalActivationResponse(false, "cardGuid не может быть null");
        }

        // 1. Проверка карты
        Card card = cardRepository.findByCardGuid(request.getCardGuid())
                .orElse(null);
        if (card == null) {
            return new TerminalActivationResponse(false, "Карта с таким GUID не найдена");
        }

        // 2. Проверка типа карты: должна быть "Карта сброса терминала"
        if (!card.getCardType().getCardTypeId().equals(tavridaConstants.getCardTypeTerminalReset())) {
            return new TerminalActivationResponse(false, "Карта не является картой сброса терминала");
        }

        // 3. Проверка: привязана ли карта к пользователю?
        if (card.getUser() == null || card.getUser().getUserId() == null) {
            return new TerminalActivationResponse(false, "Карта не привязана ни к одному пользователю");
        }

        // 4. Проверка роли пользователя
        User user = userRepository.findById(card.getUserId())
                .orElse(null);
        if (user == null) {
            return new TerminalActivationResponse(false, "Пользователь, которому принадлежит карта, не найден");
        }

        Integer userRoleId = user.getUserRoleId();
        if (!userRoleId.equals(tavridaConstants.getUserRoleCarrierAdmin()) &&
                !userRoleId.equals(tavridaConstants.getUserRoleOperatorFundsAdmin()) &&
                !userRoleId.equals(tavridaConstants.getUserRoleCashier())
        ) {
            return new TerminalActivationResponse(false,
                    "Карта принадлежит пользователю без прав на деактивацию терминала. Требуется администратор перевозчика или оператора или кассир.");
        }

        // 5. Поиск терминала
        Terminal terminal = terminalRepository.findByTerminalGuid(request.getTerminalGuid())
                .orElse(null);
        if (terminal == null) {
            return new TerminalActivationResponse(false, "Терминал с таким GUID не найден");
        }

        // 6. Отвязка от перевозчика (и на всякий случай от транспотра)
        terminal.setCarrier(null);
        terminal.setTransport(null);
        terminalRepository.save(terminal);

        return new TerminalActivationResponse(true,
                "Терминал успешно отвязан от перевозчика", TavridaMapper.toTerminalDto(terminal));
    }

    public CashierLogoutResponse cashierLogout(CashierLogoutRequest request) {
        UserSession session = userSessionRepository.findById(request.getSessionId()).orElseThrow(() -> new EntityNotFoundException("Смена водителя не найдена: " + request.getSessionId()));
        session.setClosedAt(Instant.now());
        userSessionRepository.save(session);

        // 9. Возвращаем ответ — транспорт берём из терминала (или из request, они совпадают)
        LocalDateTime now = LocalDateTime.now();
        return new CashierLogoutResponse(
                true,
                "Сессия кассира успешно закрыта",
                request.getSessionId(),
                null,
                now,
                TavridaMapper.toUserDto(session.getUser())
        );
    }

}