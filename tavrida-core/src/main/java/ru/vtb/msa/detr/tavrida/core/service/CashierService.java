package ru.vtb.msa.detr.tavrida.core.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.vtb.msa.detr.tavrida.api.model.PaymentDto;
import ru.vtb.msa.detr.tavrida.api.model.PaymentResultDto;
import ru.vtb.msa.detr.tavrida.api.model.PaymentTypeDto;
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
import java.time.ZoneOffset;
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
    private final ServiceEventRepository serviceEventRepository;
    private final ServiceEventTypeRepository serviceEventTypeRepository;
    private final ObjectMapper objectMapper;


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
            TavridaUtils util,
            ServiceEventRepository serviceEventRepository,
            ServiceEventTypeRepository serviceEventTypeRepository,
            ObjectMapper objectMapper
            ) {
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
        this.serviceEventRepository = serviceEventRepository;
        this.serviceEventTypeRepository = serviceEventTypeRepository;
        this.objectMapper = objectMapper;

    }

    @Transactional
    public TerminalActivationResponse activateTerminal(CashierTerminalActivationRequest request) {

        final Instant now1 = Instant.now();
        final Instant localTime = request.getActivationLocalStartTime().toInstant(ZoneOffset.of(request.getTimeZoneOffset()));

        if (request.getTerminalGuid() == null) {
            request.setTerminalGuid(UUID.randomUUID());
            request.setTerminalNumber(util.generateCode());
        }
        if (request.getCardGuid() == null) {
            return new TerminalActivationResponse(false, "cardGuid не может быть null");
        }
//        if (request.getCarrierId() == null) {
//            return new TerminalActivationResponse(false, "carrierId не может быть null");
//        }

        Carrier carrier = null;
        if (request.getCarrierId() != null) {
            carrier = carrierRepository.findById(request.getCarrierId()).orElse(null);
        }

        // 2. Проверка терминала
        Terminal terminal = terminalRepository.findByTerminalGuid(request.getTerminalGuid())
                .orElse(null);
        if (terminal != null) {
            if (terminal.getTransport() != null) {
                if (carrier != null) {
                    if (!Objects.equals(terminal.getCarrier().getCarrierId(), carrier.getCarrierId())) {
                        return new TerminalActivationResponse(false, "Терминал с таким GUID уже привязан к другому перевозчику!");
                    } else {
                        // 6. Если уже привязан к тому же — ничего не делаем (идемпотентность)
                        return new TerminalActivationResponse(true,
                                "Терминал уже привязан к перевозчику ID " + carrier.getCarrierId(), TavridaMapper.toTerminalDto(terminal));
                    }
                }
            }
        } else {
            terminal = new Terminal();
            terminal.setTerminalGuid(request.getTerminalGuid());
            terminal.setTerminalNumber(request.getTerminalNumber());
            terminal.setTerminalSerialNumber(request.getTerminalSerialNumber());
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


        TerminalActivationResponse terminalActivationResponse = new TerminalActivationResponse(true,
                "Терминал успешно активирован",
                TavridaMapper.toTerminalDto(terminal));

        // 9. serviceEventService
        ServiceEvent serviceEvent = new ServiceEvent();
        serviceEvent.setServiceEventType(
                serviceEventTypeRepository.findById(tavridaConstants.getServiceEventTypeTerminalActivation()).orElse(null)
        );
        serviceEvent.setEventTime(now1);
        serviceEvent.setEventLocalTime(localTime);
        serviceEvent.setUser(user);
        serviceEvent.setSession(null);
        serviceEvent.setEventDetails("Активация терминала кассиром '" + user.getUserFio() + "' на время " + serviceEvent.getEventTime());
        serviceEvent.setEventObject(objectMapper.valueToTree(terminalActivationResponse));
        serviceEventRepository.save(serviceEvent);

        return terminalActivationResponse;

    }


    @Transactional
    public CashierLoginResponse cashierLogin(CashierLoginRequest request) {

        final Instant now1 = Instant.now();
        final Instant localTime = request.getLoginLocalStartTime().toInstant(ZoneOffset.of(request.getTimeZoneOffset()));

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

        // 8. Создаём сессию — только терминал и пользователь
        UUID sessionId = UUID.randomUUID();
        UserSession session = new UserSession();
        session.setSessionId(sessionId);
        session.setTerminal(terminal);
        session.setUser(user);
        session.setStartedAt(now1);
        session.setStartedAtLocal(request.getLoginLocalStartTime().toInstant(ZoneOffset.of(request.getTimeZoneOffset())));
        session.setClosedAt(null);
        session.setExpirationTime(now1.plus(31, ChronoUnit.DAYS));
        UserSession savedSession = userSessionRepository.save(session);

        CashierLoginResponse cashierLoginResponse = new CashierLoginResponse(TavridaMapper.toUserSessionDto(savedSession));

        // 9. serviceEventService
        ServiceEvent serviceEvent = new ServiceEvent();
        serviceEvent.setServiceEventType(
                serviceEventTypeRepository.findById(tavridaConstants.getServiceEventTypeCashierSessionStart()).orElse(null)
        );
        serviceEvent.setEventTime(now1);
        serviceEvent.setEventLocalTime(localTime);
        serviceEvent.setUser(user);
        serviceEvent.setSession(savedSession);
        serviceEvent.setEventDetails("Активация сессии кассира '" + user.getUserFio() + "' на время " + serviceEvent.getEventTime());
        serviceEvent.setEventObject(objectMapper.valueToTree(cashierLoginResponse));
        serviceEventRepository.save(serviceEvent);

        return cashierLoginResponse;
    }

    @Transactional
    public CardInitResponse initCard(CardInitRequest request) {

        final Instant now1 = Instant.now();
        final Instant localTime = request.getLocalStartTime().toInstant(ZoneOffset.of(request.getTimeZoneOffset()));

        UserSession session = sessionService.getSessionEntity(request.getSessionId()); // валидация

        Card newCard = new Card();
        newCard.setCardGuid(UUID.randomUUID());
        newCard.setCardType(cardTypeService.getCardTypeEntityById(tavridaConstants.getCardTypePassenger())); // Пассажирская
        newCard.setMaximumUniqueCount(0);
        newCard.setUniqueTravelCount(0);
        newCard.setAvailableTravelCount(0);
        newCard.setExpirationRidesPackage(now1.plus(31, ChronoUnit.DAYS));
        newCard.setUnlimitedUntilDate(now1.plus(31, ChronoUnit.DAYS));
        newCard.setIssueDate(now1);
        newCard.setExpirationDate(now1.plus(365 * 5, ChronoUnit.DAYS));
        Card savedCard = cardService.save(newCard);

        CardInitResponse cardInitResponse = new CardInitResponse(
                savedCard.getCardGuid(),
                LocalDateTime.ofInstant(savedCard.getExpirationDate(), ZoneOffset.of(request.getTimeZoneOffset())));

        // 9. serviceEventService
        ServiceEvent serviceEvent = new ServiceEvent();
        serviceEvent.setServiceEventType(
                serviceEventTypeRepository.findById(tavridaConstants.getServiceEventTypeCardCreate()).orElse(null)
        );
        serviceEvent.setEventTime(now1);
        serviceEvent.setEventLocalTime(localTime);
        serviceEvent.setUser(session.getUser());
        serviceEvent.setSession(session);
        serviceEvent.setEventDetails("Активация сессии кассира '" + session.getUser().getUserFio() + "' на время " + serviceEvent.getEventTime());
        serviceEvent.setEventObject(objectMapper.valueToTree(cardInitResponse));
        serviceEventRepository.save(serviceEvent);

        return cardInitResponse;
    }

    @Transactional
    public CardBalanceResponse getCardBalance(CardBalanceRequest request) {

        final Instant now1 = Instant.now();
        final Instant localTime = request.getLocalStartTime().toInstant(ZoneOffset.of(request.getTimeZoneOffset()));

        UserSession session = sessionService.getSessionEntity(request.getSessionId()); // валидация

        Card card = cardService.getCardEntityByGuid(request.getCardUuid());
        card.setAvailableTravelCount(Math.min(card.getAvailableTravelCount(), request.getCurrentTripsCount()));
        cardService.save(card);

        CardBalanceResponse cardBalanceResponse = new CardBalanceResponse(card.getAvailableTravelCount());

        // 9. serviceEventService
        ServiceEvent serviceEvent = new ServiceEvent();
        serviceEvent.setServiceEventType(
                serviceEventTypeRepository.findById(tavridaConstants.getServiceEventTypeOther()).orElse(null)
        );
        serviceEvent.setEventTime(now1);
        serviceEvent.setEventLocalTime(localTime);
        serviceEvent.setUser(session.getUser());
        serviceEvent.setSession(session);
        serviceEvent.setEventDetails("Получен баланс карты '" + card.getCardGuid() + "' на время " + serviceEvent.getEventTime());
        serviceEvent.setEventObject(objectMapper.valueToTree(cardBalanceResponse));
        serviceEventRepository.save(serviceEvent);

        return cardBalanceResponse;
    }

    @Transactional
    public CardPurchaseResponse purchaseTrips(CardPurchaseRequest request) {

        final Instant now1 = Instant.now();
        final Instant localTime = request.getLocalStartTime().toInstant(ZoneOffset.of(request.getTimeZoneOffset()));

        UserSession session = sessionService.getSessionEntity(request.getSessionId()); // валидация

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
        payment.setTerminalId(session.getTerminal().getTerminalId());
        PaymentDto savedPayment = paymentService.createPayment(payment);

        CardPurchaseResponse cardPurchaseResponse = new CardPurchaseResponse(
                updatedCard.getCardGuid(),
                updatedCard.getAvailableTravelCount(),
                savedPayment.getPaymentId()
        );

        // 9. serviceEventService
        ServiceEvent serviceEvent = new ServiceEvent();
        serviceEvent.setServiceEventType(
                serviceEventTypeRepository.findById(tavridaConstants.getServiceEventTypeCardRecharge()).orElse(null)
        );
        serviceEvent.setEventTime(now1);
        serviceEvent.setEventLocalTime(localTime);
        serviceEvent.setUser(session.getUser());
        serviceEvent.setSession(session);
        serviceEvent.setEventDetails("Пополнен баланс карты '" + card.getCardGuid() + "' на время " + serviceEvent.getEventTime());
        serviceEvent.setEventObject(objectMapper.valueToTree(cardPurchaseResponse));
        serviceEventRepository.save(serviceEvent);

        return cardPurchaseResponse;
    }

    @Transactional
    public TerminalActivationResponse deactivateTerminal(TerminalDeactivationRequest request) {

        final Instant now1 = Instant.now();
        final Instant localTime = request.getTerminalDeactivationStartTime().toInstant(ZoneOffset.of(request.getTimeZoneOffset()));

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

        TerminalActivationResponse terminalActivationResponse = new TerminalActivationResponse(true,
                "Терминал успешно отвязан от перевозчика", TavridaMapper.toTerminalDto(terminal));

        // 9. serviceEventService
        ServiceEvent serviceEvent = new ServiceEvent();
        serviceEvent.setServiceEventType(
                serviceEventTypeRepository.findById(tavridaConstants.getServiceEventTypeTerminalDeactivation()).orElse(null)
        );
        serviceEvent.setEventTime(now1);
        serviceEvent.setEventLocalTime(localTime);
        serviceEvent.setUser(user);
        serviceEvent.setSession(null);
        serviceEvent.setEventDetails("Получен баланс карты '" + card.getCardGuid() + "' на время " + serviceEvent.getEventTime());
        serviceEvent.setEventObject(objectMapper.valueToTree(terminalActivationResponse));
        serviceEventRepository.save(serviceEvent);

        return terminalActivationResponse;
    }

    @Transactional
    public CashierLogoutResponse cashierLogout(CashierLogoutRequest request) {

        final Instant now1 = Instant.now();
        final Instant localTime = request.getLogoutStartTime().toInstant(ZoneOffset.of(request.getTimeZoneOffset()));

        UserSession session = userSessionRepository.findById(request.getSessionId()).orElseThrow(() -> new EntityNotFoundException("Смена водителя не найдена: " + request.getSessionId()));
        session.setClosedAt(Instant.now());
        userSessionRepository.save(session);

        // 9. Возвращаем ответ — транспорт берём из терминала (или из request, они совпадают)
        LocalDateTime now = LocalDateTime.ofInstant(now1, ZoneOffset.of(request.getTimeZoneOffset()));
        CashierLogoutResponse cashierLogoutResponse = new CashierLogoutResponse(
                true,
                "Сессия кассира успешно закрыта",
                request.getSessionId(),
                null,
                now,
                TavridaMapper.toUserDto(session.getUser())
        );

        // 9. serviceEventService
        ServiceEvent serviceEvent = new ServiceEvent();
        serviceEvent.setServiceEventType(
                serviceEventTypeRepository.findById(tavridaConstants.getServiceEventTypeCardCreate()).orElse(null)
        );
        serviceEvent.setEventTime(now1);
        serviceEvent.setEventLocalTime(localTime);
        serviceEvent.setUser(session.getUser());
        serviceEvent.setSession(session);
        serviceEvent.setEventDetails(cashierLogoutResponse.getMessage());
        serviceEvent.setEventObject(objectMapper.valueToTree(cashierLogoutResponse));
        serviceEventRepository.save(serviceEvent);

        return cashierLogoutResponse;
    }

}