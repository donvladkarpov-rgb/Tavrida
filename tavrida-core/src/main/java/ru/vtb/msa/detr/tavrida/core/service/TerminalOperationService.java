package ru.vtb.msa.detr.tavrida.core.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.vtb.msa.detr.tavrida.api.model.terminal.DriverSessionResponse;
import ru.vtb.msa.detr.tavrida.api.model.terminal.DriverSessionStartRequest;
import ru.vtb.msa.detr.tavrida.api.model.terminal.DriverSessionStopRequest;
import ru.vtb.msa.detr.tavrida.api.model.terminal.DriverTripResponse;
import ru.vtb.msa.detr.tavrida.api.model.terminal.DriverTripStartRequest;
import ru.vtb.msa.detr.tavrida.api.model.terminal.DriverTripStopRequest;
import ru.vtb.msa.detr.tavrida.api.model.terminal.TerminalActivationRequest;
import ru.vtb.msa.detr.tavrida.api.model.terminal.TerminalActivationResponse;
import ru.vtb.msa.detr.tavrida.api.model.terminal.TerminalDeactivationRequest;
import ru.vtb.msa.detr.tavrida.api.model.terminal.TerminalDeductRequest;
import ru.vtb.msa.detr.tavrida.api.model.terminal.TerminalDeductResponse;
import ru.vtb.msa.detr.tavrida.core.config.TavridaConstants;
import ru.vtb.msa.detr.tavrida.core.exception.EntityNotFoundException;
import ru.vtb.msa.detr.tavrida.core.exception.ValidationTavridaException;
import ru.vtb.msa.detr.tavrida.core.model.BlackList;
import ru.vtb.msa.detr.tavrida.core.model.Card;
import ru.vtb.msa.detr.tavrida.core.model.Route;
import ru.vtb.msa.detr.tavrida.core.model.ServiceEvent;
import ru.vtb.msa.detr.tavrida.core.model.Terminal;
import ru.vtb.msa.detr.tavrida.core.model.Transport;
import ru.vtb.msa.detr.tavrida.core.model.Trip;
import ru.vtb.msa.detr.tavrida.core.model.User;
import ru.vtb.msa.detr.tavrida.core.model.UserSession;
import ru.vtb.msa.detr.tavrida.core.model.mapper.TavridaMapper;
import ru.vtb.msa.detr.tavrida.core.model.mapper.TripMapper;
import ru.vtb.msa.detr.tavrida.core.repo.BlackListRepository;
import ru.vtb.msa.detr.tavrida.core.repo.CardRepository;
import ru.vtb.msa.detr.tavrida.core.repo.CarrierRouteMapRepository;
import ru.vtb.msa.detr.tavrida.core.repo.PaymentRepository;
import ru.vtb.msa.detr.tavrida.core.repo.RouteRepository;
import ru.vtb.msa.detr.tavrida.core.repo.ServiceEventRepository;
import ru.vtb.msa.detr.tavrida.core.repo.ServiceEventTypeRepository;
import ru.vtb.msa.detr.tavrida.core.repo.TerminalRepository;
import ru.vtb.msa.detr.tavrida.core.repo.TransportRepository;
import ru.vtb.msa.detr.tavrida.core.repo.TripRepository;
import ru.vtb.msa.detr.tavrida.core.repo.UserRepository;
import ru.vtb.msa.detr.tavrida.core.repo.UserSessionRepository;
import ru.vtb.msa.detr.tavrida.core.util.TavridaUtils;

import java.time.Instant;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class TerminalOperationService {

    private final CardRepository cardRepository;
    private final BlackListRepository blackListRepository;
    private final PaymentRepository paymentRepository;
    private final TerminalRepository terminalRepository;
    private final TransportRepository transportRepository;
    private final TavridaConstants tavridaConstants;
    private final UserRepository userRepository;
    private final UserSessionRepository userSessionRepository;
    private final SessionService sessionService;
    private final TavridaUtils util;
    private final RouteRepository routeRepository;
    private final CarrierRouteMapRepository carrierRouteMapRepository;
    private final TripRepository tripRepository;
    private final ServiceEventRepository serviceEventRepository;
    private final ServiceEventTypeRepository serviceEventTypeRepository;
    private final ObjectMapper objectMapper;

    public TerminalOperationService(
        SessionService sessionService,
        CardRepository cardRepository,
        BlackListRepository blackListRepository,
        PaymentRepository paymentRepository,
        TerminalRepository terminalRepository,
        TransportRepository transportRepository,
        TavridaConstants tavridaConstants,
        UserRepository userRepository,
        UserSessionRepository userSessionRepository,
        TavridaUtils util,
        RouteRepository routeRepository,
        CarrierRouteMapRepository carrierRouteMapRepository,
        TripRepository tripRepository,
        ServiceEventRepository serviceEventRepository,
        ServiceEventTypeRepository serviceEventTypeRepository,
        ObjectMapper objectMapper) {
        this.cardRepository = cardRepository;
        this.blackListRepository = blackListRepository;
        this.paymentRepository = paymentRepository;
        this.terminalRepository = terminalRepository;
        this.transportRepository = transportRepository;
        this.tavridaConstants = tavridaConstants;
        this.userRepository = userRepository;
        this.userSessionRepository = userSessionRepository;
        this.sessionService = sessionService;
        this.util = util;
        this.routeRepository = routeRepository;
        this.carrierRouteMapRepository = carrierRouteMapRepository;
        this.tripRepository = tripRepository;
        this.serviceEventRepository = serviceEventRepository;
        this.serviceEventTypeRepository = serviceEventTypeRepository;
        this.objectMapper = objectMapper;
    }

    @Transactional
    public DriverSessionResponse startDriverSession(DriverSessionStartRequest request) {

        final Instant now1 = Instant.now();
        final Instant localTime = util.parseInstant(request.getSessionStartTime());

        // Валидация
        if (request.getTerminalGuid() == null) {
            return new DriverSessionResponse(false, "terminalGuid не может быть null");
        }
        if (request.getTransportGuid() == null) {
            return new DriverSessionResponse(false, "transportGuid не может быть null");
        }
        if (request.getDriverCardGuid() == null) {
            return new DriverSessionResponse(false, "driverCardGuid не может быть null");
        }

        // 1. Проверяем транспорт (существует ли)
        Transport transport = transportRepository.findByTransportGuid(request.getTransportGuid())
            .orElse(null);
        if (transport == null) {
            return new DriverSessionResponse(false, "Транспорт не найден: " + request.getTransportGuid());
        }

        // 2. Находим терминал
        Terminal terminal = terminalRepository.findByTerminalGuid(request.getTerminalGuid())
            .orElse(null);
        if (terminal == null) {
            return new DriverSessionResponse(false, "Терминал не найден: " + request.getTerminalGuid());
        }

        // 3. Проверяем, что терминал привязан именно к этому транспорту
        if (terminal.getTransport() == null) {
            return new DriverSessionResponse(false, "Терминал не привязан ни к какому транспорту");
        }
        if (!Objects.equals(terminal.getTransport().getTransportGuid(), request.getTransportGuid())) {
            return new DriverSessionResponse(false,
                "Терминал привязан к другому транспорту (ожидается: " + request.getTransportGuid() +
                    ", фактически: " + terminal.getTransport().getTransportGuid() + ")");
        }

        // 4. Проверяем карту водителя
        Card driverCard = cardRepository.findByCardGuid(request.getDriverCardGuid())
            .orElse(null);
        if (driverCard == null) {
            return new DriverSessionResponse(false, "Карта водителя не найдена");
        }

        // 5. Проверка типа карты — водительская
        if (!Objects.equals(driverCard.getCardType().getCardTypeId(), tavridaConstants.getCardTypeDriver())) {
            return new DriverSessionResponse(false, "Карта не является водительской");
        }

        // 6. Проверка пользователя
        if (driverCard.getUser() == null || driverCard.getUser().getUserId() == null) {
            return new DriverSessionResponse(false, "Карта не привязана к пользователю");
        }

        User driver = userRepository.findById(driverCard.getUserId())
            .orElse(null);
        if (driver == null) {
            return new DriverSessionResponse(false, "Пользователь-водитель не найден");
        }

        // 7. Проверка роли
        if (!Objects.equals(driver.getUserRoleId(), tavridaConstants.getUserRoleDriver())) {
            return new DriverSessionResponse(false, "Пользователь не имеет роли водителя");
        }

        // 8. Создаём сессию — только терминал и пользователь
        UUID sessionId = UUID.randomUUID();
        UserSession session = new UserSession();
        session.setSessionId(sessionId);
        session.setTerminal(terminal);
        session.setUser(driver);
        session.setStartedAt(Instant.now());
        session.setStartedAtLocal(util.parseInstant(request.getSessionStartTime()));
        session.setClosedAt(null);
        session.setExpirationTime(Instant.now().plus(31, ChronoUnit.DAYS));
        userSessionRepository.save(session);

        // 10. Возвращаем ответ — транспорт берём из терминала (или из request, они совпадают)
        DriverSessionResponse driverSessionResponse = new DriverSessionResponse(
            true,
            "Сессия водителя успешно начата",
            sessionId,
            request.getDriverCardGuid(),
            request.getTransportGuid(), // или terminal.getTransport().getTransportGuid()
            DateTimeFormatter.ISO_INSTANT.format(Instant.now()),
            TavridaMapper.toUserDto(driver)
        );

        // 9. serviceEventService
        ServiceEvent serviceEvent = new ServiceEvent();
        serviceEvent.setServiceEventType(
            serviceEventTypeRepository.findById(tavridaConstants.getServiceEventTypeDriverSessionStart()).orElse(null)
        );
        serviceEvent.setEventTime(now1);
        serviceEvent.setEventLocalTime(localTime);
        serviceEvent.setUser(driver);
        serviceEvent.setSession(session);
        serviceEvent.setEventDetails("Старт смены водителя '" + driver.getUserFio() + "' на '" + terminal.getTransport().getTransportNumber() + "' время " + serviceEvent.getEventTime());
        serviceEvent.setEventObject(objectMapper.valueToTree(driverSessionResponse));
        serviceEventRepository.save(serviceEvent);

        return driverSessionResponse;
    }

    @Transactional
    public DriverSessionResponse stopDriverSession(DriverSessionStopRequest request) {

        final Instant now1 = Instant.now();
        final Instant localTime = util.parseInstant(request.getSessionStartTime());

        UserSession session = userSessionRepository.findById(request.getSessionId()).orElseThrow(() -> new EntityNotFoundException("Смена водителя не найдена: " + request.getSessionId()));
        session.setClosedAt(now1);
        session.setClosedAtLocal(localTime);
        userSessionRepository.save(session);

        // 9. Возвращаем ответ — транспорт берём из терминала (или из request, они совпадают)
        DriverSessionResponse driverSessionResponse = new DriverSessionResponse(
            true,
            "Сессия водителя успешно закрыта",
            request.getSessionId(),
            null,
            null, // или terminal.getTransport().getTransportGuid()

            DateTimeFormatter.ISO_INSTANT.format(now1),
            TavridaMapper.toUserDto(session.getUser())
        );

        // 9. serviceEventService
        ServiceEvent serviceEvent = new ServiceEvent();
        serviceEvent.setServiceEventType(
            serviceEventTypeRepository.findById(tavridaConstants.getServiceEventTypeDriverSessionStop()).orElse(null)
        );
        serviceEvent.setEventTime(now1);
        serviceEvent.setEventLocalTime(localTime);
        serviceEvent.setUser(session.getUser());
        serviceEvent.setSession(session);
        serviceEvent.setEventDetails("Старт смены водителя '" + session.getUser().getUserFio() + "' на '" + session.getTerminal().getTransport().getTransportNumber() + "' время " + serviceEvent.getEventTime());
        serviceEvent.setEventObject(objectMapper.valueToTree(driverSessionResponse));
        serviceEventRepository.save(serviceEvent);

        return driverSessionResponse;

    }

    @Transactional
    public TerminalActivationResponse activateTerminal(TerminalActivationRequest request) {

        final Instant now1 = Instant.now();
        final Instant localTime = util.parseInstant(request.getTerminalActivationStartTime());

        if (request.getTerminalGuid() == null) {
            request.setTerminalGuid(UUID.randomUUID());
            request.setTerminalNumber(util.generateCode());
        }
        if (request.getTransportGuid() == null) {
            return new TerminalActivationResponse(false, "transport guid не может быть null");
        }
        if (request.getCardGuid() == null) {
            return new TerminalActivationResponse(false, "cardGuid не может быть null");
        }

        // 1. Проверка транспорта
        Transport transport = transportRepository.findByTransportGuid(request.getTransportGuid())
            .orElse(null);
        if (transport == null) {
            return new TerminalActivationResponse(false, "Транспорт с GUID " + request.getTransportGuid() + " не найден");
        }

        // 2. Проверка терминала
        Terminal terminal = terminalRepository.findByTerminalGuid(request.getTerminalGuid())
            .orElse(null);
        if (terminal != null) {
            if (terminal.getTransport() != null) {
                if (!Objects.equals(terminal.getTransport().getTransportId(), transport.getTransportId())) {
                    return new TerminalActivationResponse(false, "Терминал с таким GUID уже привязан к другому транспорту!");
                } else {
                    // 6. Если уже привязан к тому же — ничего не делаем (идемпотентность)
                    return new TerminalActivationResponse(true,
                        "Терминал уже привязан к транспорту ID " + transport.getTransportId(), TavridaMapper.toTerminalDto(terminal));
                }
            }
        } else {
            terminal = new Terminal();
            terminal.setTerminalGuid(request.getTerminalGuid());
            terminal.setTerminalNumber(request.getTerminalNumber());
            terminal.setTerminalSerialNumber(request.getTerminalNumber());
        }
        terminal.setTransport(transport);
        terminal.setCarrier(null);

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
            !userRoleId.equals(tavridaConstants.getUserRoleOperatorFundsAdmin())) {
            return new TerminalActivationResponse(false,
                "Карта принадлежит пользователю без прав на активацию терминала. Требуется администратор перевозчика или оператора.");
        }

        // 7. Привязываем
        terminalRepository.save(terminal);


        TerminalActivationResponse terminalActivationResponse = new TerminalActivationResponse(true,
            "Терминал успешно привязан к транспорту ID " + transport.getTransportId(),
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
        serviceEvent.setEventDetails("Активация терминала '" + terminal.getTerminalNumber() + "' на '" + transport.getTransportNumber() + "' время " + serviceEvent.getEventTime());
        serviceEvent.setEventObject(objectMapper.valueToTree(terminalActivationResponse));
        serviceEventRepository.save(serviceEvent);

        return terminalActivationResponse;

    }

    @Transactional
    public TerminalActivationResponse deactivateTerminal(TerminalDeactivationRequest request) {

        final Instant now1 = Instant.now();
        final Instant localTime = util.parseInstant(request.getTerminalDeactivationStartTime());

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
            !userRoleId.equals(tavridaConstants.getUserRoleOperatorFundsAdmin())) {
            return new TerminalActivationResponse(false,
                "Карта принадлежит пользователю без прав на деактивацию терминала. Требуется администратор перевозчика или оператора.");
        }

        // 5. Поиск терминала
        Terminal terminal = terminalRepository.findByTerminalGuid(request.getTerminalGuid())
            .orElse(null);
        if (terminal == null) {
            return new TerminalActivationResponse(false, "Терминал с таким GUID не найден");
        }

        // 6. Отвязка от транспорта
        terminal.setTransport(null);
        terminalRepository.save(terminal);

        TerminalActivationResponse terminalActivationResponse = new TerminalActivationResponse(true,
            "Терминал успешно отвязан от транспорта", TavridaMapper.toTerminalDto(terminal));

        // 9. serviceEventService
        ServiceEvent serviceEvent = new ServiceEvent();
        serviceEvent.setServiceEventType(
            serviceEventTypeRepository.findById(tavridaConstants.getServiceEventTypeTerminalDeactivation()).orElse(null)
        );
        serviceEvent.setEventTime(now1);
        serviceEvent.setEventLocalTime(localTime);
        serviceEvent.setUser(user);
        serviceEvent.setSession(null);
        serviceEvent.setEventDetails(terminalActivationResponse.getMessage());
        serviceEvent.setEventObject(objectMapper.valueToTree(terminalActivationResponse));
        serviceEventRepository.save(serviceEvent);

        return terminalActivationResponse;
    }

    /**
     * Списание поездки с карты
     */
    @Transactional
    public TerminalDeductResponse deductTrip(TerminalDeductRequest request) {

        final Instant now1 = Instant.now();
        final Instant localTime = util.parseInstant(request.getTerminalDeductStartTime());

        Trip trip = tripRepository.findById(request.getTripId()).orElseThrow(() -> new EntityNotFoundException("Рейс водителя не найден: " + request.getTripId()));
        UserSession session = userSessionRepository.findById(request.getSessionId()).orElseThrow(() -> new EntityNotFoundException("Смена водителя не найдена: " + request.getSessionId()));

        UUID cardGuid = request.getCardGuid();
        UUID terminalGuid = request.getTerminalGuid();
        int terminalBalance = request.getTerminalBalance();

        // 1. Проверяем чёрный список
        if (isCardBlocked(cardGuid)) {
            throw new ValidationTavridaException("Карта заблокирована: " + cardGuid);
        }

        // 3. Находим карту на сервере
        Card serverCard = cardRepository.findByCardGuid(cardGuid)
            .orElseThrow(() -> new EntityNotFoundException("Карта не найдена: " + cardGuid));

        // 4. Определяем актуальный баланс — минимальный из двух
        int actualBalance = Math.min(serverCard.getAvailableTravelCount(), terminalBalance);
        if (actualBalance <= 0) {
            throw new ValidationTavridaException("Недостаточно поездок на карте: " + cardGuid);
        }

        // 5. Обновляем баланс на сервере (мастер баланс находится на карте у пассажира)
        serverCard.setAvailableTravelCount(actualBalance);
        cardRepository.save(serverCard);

        TerminalDeductResponse terminalDeductResponse = new TerminalDeductResponse();
        terminalDeductResponse.setSuccess(true);
        terminalDeductResponse.setMessage("Проезд оплачен.");
        terminalDeductResponse.setTripDto(TripMapper.toDto(trip));
        terminalDeductResponse.setSessionDto(TavridaMapper.toUserSessionDto(session));
        terminalDeductResponse.setCardDto(TavridaMapper.toCardDto(serverCard));

        // 9. serviceEventService
        ServiceEvent serviceEvent = new ServiceEvent();
        serviceEvent.setServiceEventType(
            serviceEventTypeRepository.findById(tavridaConstants.getServiceEventTypePaymentTransit()).orElse(null)
        );
        serviceEvent.setEventTime(now1);
        serviceEvent.setEventLocalTime(localTime);
        serviceEvent.setUser(null);
        serviceEvent.setSession(session);
        serviceEvent.setEventDetails(terminalDeductResponse.getMessage());
        serviceEvent.setEventObject(objectMapper.valueToTree(terminalDeductResponse));
        serviceEventRepository.save(serviceEvent);

        return terminalDeductResponse;
    }

    /**
     * Проверка, заблокирована ли карта
     */
    public boolean isCardBlocked(UUID cardGuid) {
        return blackListRepository.existsByCardGuid(cardGuid);
    }

    /**
     * Получение всего чёрного списка (для кэширования на терминале)
     */
    public Set<UUID> getFullBlackList() {
        return blackListRepository.findAll().stream()
            .map(BlackList::getCardGuid)
            .collect(Collectors.toSet());
    }

    public List<UUID> deductsTrip(List<TerminalDeductRequest> request) {
        List<UUID> result = new ArrayList<>();
        request.forEach(r -> {
            try {
                deductTrip(r);
            } catch (Exception ex) {
                result.add(r.getCardGuid());
            }
        });
        return result;
    }

    @Transactional
    public DriverTripResponse startDriverTrip(DriverTripStartRequest request) {

        final Instant now1 = Instant.now();
        final Instant localTime = util.parseInstant(request.getTerminalLocalStartTime());

        // 1. Валидация входных данных
        if (request.getSessionId() == null) {
            return new DriverTripResponse(null, "ERROR", "sessionId не может быть null");
        }
        if (request.getRouteGuid() == null) {
            return new DriverTripResponse(null, "ERROR", "routeGuid не может быть null");
        }

        // 2. Проверка сессии водителя
        UserSession session = userSessionRepository.findById(request.getSessionId())
            .orElseThrow(() -> new EntityNotFoundException("Сессия не найдена: " + request.getSessionId()));

        if (session.getClosedAt() != null) {
            return new DriverTripResponse(null, "ERROR", "Сессия уже завершена");
        }

        // 3. Проверка маршрута
        Route route = routeRepository.findByRouteGuid(request.getRouteGuid())
            .orElseThrow(() -> new EntityNotFoundException("Маршрут не найден: " + request.getRouteGuid()));

        // 4. Проверка привязки маршрута к перевозчику
        boolean isRouteAssigned = carrierRouteMapRepository.existsByCarrier_CarrierIdAndRoute_RouteId(
            session.getTerminal().getTransport().getCarrier().getCarrierId(),
            route.getRouteId()
        );
        if (!isRouteAssigned) {
            return new DriverTripResponse(null, "ERROR", "Маршрут не привязан к перевозчику терминала");
        }

        // 6. Проверка, что у водителя нет активного рейса
        if (tripRepository.existsBySession_SessionIdAndClosedAtIsNull(request.getSessionId())) {
            return new DriverTripResponse(null, "ERROR", "У водителя уже есть активный рейс");
        }

        // 7. Создание рейса
        Trip trip = new Trip();
        trip.setRoute(route);
        trip.setSession(session);
        trip.setStartedAt(now1);
        trip.setClosedAt(null); // будет закрыт позже
        trip.setStartedAtLocal(localTime);
        trip.setClosedAtLocal(null); // будет закрыт позже

        Trip savedTrip = tripRepository.save(trip);

        DriverTripResponse driverTripResponse = new DriverTripResponse(
            TripMapper.toDto(savedTrip),
            "SUCCESS",
            "Рейс успешно начат для маршрута: " + route.getRouteName()
        );

        // 9. serviceEventService
        ServiceEvent serviceEvent = new ServiceEvent();
        serviceEvent.setServiceEventType(
            serviceEventTypeRepository.findById(tavridaConstants.getServiceEventTypeDriverRouteStart()).orElse(null)
        );
        serviceEvent.setEventTime(now1);
        serviceEvent.setEventLocalTime(localTime);
        serviceEvent.setUser(session.getUser());
        serviceEvent.setSession(session);
        serviceEvent.setEventDetails(driverTripResponse.getMessage());
        serviceEvent.setEventObject(objectMapper.valueToTree(driverTripResponse));
        serviceEventRepository.save(serviceEvent);

        return driverTripResponse;
    }

    @Transactional
    public DriverTripResponse stopDriverTrip(DriverTripStopRequest request) {

        final Instant now1 = Instant.now();
        final Instant localTime = util.parseInstant(request.getTripStopTime());

        // 1. Валидация входных данных
        if (request.getTripId() == null) {
            return new DriverTripResponse(null, "ERROR", "tripId не может быть null");
        }

        // 2. Поиск рейса
        Trip trip = tripRepository.findById(request.getTripId())
            .orElseThrow(() -> new EntityNotFoundException("Рейс не найден: " + request.getTripId()));

        // 3. Проверка, что рейс ещё не закрыт
        if (trip.getClosedAt() != null) {
            return new DriverTripResponse(null, "ERROR", "Рейс уже завершён");
        }

        // 4. Закрываем рейс
        trip.setClosedAt(now1);
        trip.setClosedAtLocal(localTime);
        tripRepository.save(trip);

        DriverTripResponse driverTripResponse = new DriverTripResponse(
            TripMapper.toDto(trip),
            "SUCCESS",
            "Рейс успешно завершён"
        );

        // 9. serviceEventService
        ServiceEvent serviceEvent = new ServiceEvent();
        serviceEvent.setServiceEventType(
            serviceEventTypeRepository.findById(tavridaConstants.getServiceEventTypeDriverRouteStart()).orElse(null)
        );
        serviceEvent.setEventTime(now1);
        serviceEvent.setEventLocalTime(localTime);
        serviceEvent.setUser(trip.getSession().getUser());
        serviceEvent.setSession(trip.getSession());
        serviceEvent.setEventDetails(driverTripResponse.getMessage());
        serviceEvent.setEventObject(objectMapper.valueToTree(driverTripResponse));
        serviceEventRepository.save(serviceEvent);

        return driverTripResponse;
    }

}