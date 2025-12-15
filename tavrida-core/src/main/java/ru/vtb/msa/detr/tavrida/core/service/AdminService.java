package ru.vtb.msa.detr.tavrida.core.service;

import jakarta.validation.Valid;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.vtb.msa.detr.tavrida.api.model.*;
import ru.vtb.msa.detr.tavrida.api.model.admin.*;
import ru.vtb.msa.detr.tavrida.core.config.TavridaConstants;
import ru.vtb.msa.detr.tavrida.core.exception.AuthenticationTavridaException;
import ru.vtb.msa.detr.tavrida.core.exception.EntityNotFoundException;
import ru.vtb.msa.detr.tavrida.core.model.*;
import ru.vtb.msa.detr.tavrida.core.model.mapper.TavridaMapper;
import ru.vtb.msa.detr.tavrida.core.repo.*;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Objects;
import java.util.UUID;

@Service
public class AdminService {

    private final CarrierRepository carrierRepository;
    private final TransportRepository transportRepository;
    private final TerminalRepository terminalRepository;
    private final UserRepository userRepository;
    private final UserRoleRepository userRoleRepository;
    private final CardRepository cardRepository;
    private final CardTypeRepository cardTypeRepository;
    private final CardService cardService;
    private final TavridaConstants tavridaConstants;
    private final TerminalService terminalService;
    private final SessionService sessionService;

    public AdminService(
            CarrierRepository carrierRepository,
            TransportRepository transportRepository,
            TerminalRepository terminalRepository,
            UserRepository userRepository,
            UserRoleRepository userRoleRepository,
            CardRepository cardRepository,
            CardTypeRepository cardTypeRepository,
            CardService cardService,
            TavridaConstants tavridaConstants,
            TerminalService terminalService,
            SessionService sessionService
    ) {
        this.carrierRepository = carrierRepository;
        this.transportRepository = transportRepository;
        this.terminalRepository = terminalRepository;
        this.userRepository = userRepository;
        this.userRoleRepository = userRoleRepository;
        this.cardRepository = cardRepository;
        this.cardTypeRepository = cardTypeRepository;
        this.cardService = cardService;
        this.tavridaConstants = tavridaConstants;
        this.terminalService = terminalService;
        this.sessionService = sessionService;
    }

    /**
     * Регистрация нового перевозчика
     */
    @Transactional
    public CarrierDto registerCarrier(CarrierRegistrationRequest request) {
        sessionService.getSession(request.getSessionId()); // валидация
        if (request.getCarrierName() == null || request.getCarrierName().isBlank()) {
            throw new IllegalArgumentException("Название перевозчика не может быть пустым");
        }

        Carrier carrier = new Carrier();
        carrier.setCarrierName(request.getCarrierName());

        Carrier saved = carrierRepository.save(carrier);
        return TavridaMapper.toCarrierDto(saved);
    }

    /**
     * Регистрация нового транспортного средства у существующего перевозчика
     */
    @Transactional
    public TransportDto registerTransport(TransportRegistrationRequest request) {
        sessionService.getSession(request.getSessionId()); // валидация
        if (request.getTransportGuid() == null) {
            request.setTransportGuid(UUID.randomUUID());
        }
        if (request.getCarrierId() == null) {
            throw new IllegalArgumentException("carrierId не может быть null");
        }
        if (request.getTransportNumber() == null || request.getTransportNumber().isBlank()) {
            throw new IllegalArgumentException("Номер транспорта не может быть пустым");
        }
        if (request.getTransportName() == null || request.getTransportName().isBlank()) {
            throw new IllegalArgumentException("Название транспорта не может быть пустым");
        }

        Carrier carrier = carrierRepository.findById(request.getCarrierId())
                .orElseThrow(() -> new IllegalArgumentException("Перевозчик с ID " + request.getCarrierId() + " не найден"));

        // Проверка на уникальность transportGuid (опционально — можно вынести в базу)
        if (transportRepository.existsByTransportGuid(request.getTransportGuid())) {
            throw new IllegalArgumentException("Транспорт с таким GUID уже зарегистрирован");
        }

        Transport transport = new Transport();
        transport.setTransportGuid(request.getTransportGuid());
        transport.setCarrier(carrier);
        transport.setTransportNumber(request.getTransportNumber());
        transport.setTransportName(request.getTransportName());

        Transport saved = transportRepository.save(transport);
        return TavridaMapper.toTransportDto(saved);
    }

    /**
     * Регистрация терминала на существующем транспортном средстве
     */
    @Transactional
    public TerminalDto registerTerminal(TerminalRegistrationRequest request) {
        sessionService.getSession(request.getSessionId()); // валидация
        if (request.getTerminalGuid() == null) {
            request.setTerminalGuid(UUID.randomUUID());
        }
//        if (request.getTransportId() == null) {
//            throw new IllegalArgumentException("transportId не может быть null");
//        }
        if (request.getTerminalNumber() == null || request.getTerminalNumber().isBlank()) {
            throw new IllegalArgumentException("Номер терминала не может быть пустым");
        }

        // Найдём транспорт и убедимся, что он существует
        Transport transport = transportRepository.findById(request.getTransportId())
                .orElse(null);
        Carrier carrier = carrierRepository.findById(request.getCarrierId())
                .orElse(null);

        // Опционально: можно проверить, что терминал с таким GUID ещё не зарегистрирован
        if (terminalRepository.existsByTerminalGuid(request.getTerminalGuid())) {
            throw new IllegalArgumentException("Терминал с таким GUID уже зарегистрирован");
        }

        Terminal terminal = new Terminal();
        terminal.setTerminalGuid(request.getTerminalGuid());
        terminal.setTransport(transport);
        terminal.setCarrier(carrier);
        terminal.setTerminalNumber(request.getTerminalNumber());

        Terminal saved = terminalRepository.save(terminal);
        return TavridaMapper.toTerminalDto(saved);
    }

    /**
     * Регистрация нового пользователя
     */
    @Transactional
    public UserDto registerUser(UserRegistrationRequest request) {
        sessionService.getSession(request.getSessionId()); // валидация
        if (request.getUserFio() == null || request.getUserFio().isBlank()) {
            throw new IllegalArgumentException("ФИО пользователя не может быть пустым");
        }
        if (request.getUserRoleId() == null) {
            throw new IllegalArgumentException("Роль пользователя обязательна");
        }

        // Найти роль
        UserRole userRole = userRoleRepository.findById(request.getUserRoleId())
                .orElseThrow(() -> new IllegalArgumentException("Роль с ID " + request.getUserRoleId() + " не найдена"));

        // Найти перевозчика, если указан
        Carrier carrier = null;
        if (request.getCarrierId() != null)
            carrier = carrierRepository.findById(request.getCarrierId()).orElse(null);

        // Валидация: некоторые роли (например, "Администратор оператора") не должны быть привязаны к перевозчику
        // Это можно вынести в отдельную логику, если нужно

        User user = new User();
        user.setUserFio(request.getUserFio());
        user.setUserRole(userRole);
        user.setCarrier(carrier);

        // Хэширование пароля (если передаётся plaintext)
        if (request.getPassword() != null && !request.getPassword().isBlank()) {
            user.setUserPasswordHash(request.getPassword());
        } else {
            // Или генерировать временный пароль, или требовать его обязательного указания
            throw new IllegalArgumentException("Пароль пользователя не может быть пустым");
        }

        User saved = userRepository.save(user);
        return TavridaMapper.toUserDto(saved);
    }

    /**
     * Регистрация транспортной карты
     */
    @Transactional
    public CardDto registerCard(CardRegistrationRequest request) {
        sessionService.getSession(request.getSessionId()); // валидация
        // Валидация обязательных полей
        if (request.getCardGuid() == null) {
            request.setCardGuid(UUID.randomUUID());
        }
        if (request.getCardTypeId() == null) {
            throw new IllegalArgumentException("Тип карты обязателен");
        }
        if (request.getAvailableTravelCount() == null) {
            throw new IllegalArgumentException("availableTravelCount обязателен");
        }

        // Проверка уникальности cardGuid
        if (cardRepository.existsByCardGuid(request.getCardGuid())) {
            throw new IllegalArgumentException("Карта с таким GUID уже существует");
        }

        // Найти тип карты
        CardType cardType = cardTypeRepository.findById(request.getCardTypeId())
                .orElseThrow(() -> new IllegalArgumentException("Тип карты с ID " + request.getCardTypeId() + " не найден"));

        // Найти пользователя
        User user = null;
        if (request.getUserId() != null)
            user = userRepository.findById(request.getUserId()).orElse(null);

        Transport transport = null;
        if (request.getTransportGuid() != null)
            transport = transportRepository.findByTransportGuid(request.getTransportGuid()).orElse(null);

        // Проверка логических ограничений
        if (request.getAvailableTravelCount() < 0) {
            throw new IllegalArgumentException("availableTravelCount не может быть отрицательным");
        }

        // Создание карты
        Card card = new Card();
        card.setCardGuid(request.getCardGuid());
        card.setCardType(cardType);
        card.setUser(user);
        card.setTransport(transport);
        card.setUniqueTravelCount(request.getUniqueTravelCount());
        card.setMaximumUniqueCount(request.getMaximumUniqueCount());
        card.setAvailableTravelCount(request.getAvailableTravelCount());
        card.setExpirationDate(request.getExpirationDate()); // может быть null

        Card saved = cardRepository.save(card);
        return TavridaMapper.toCardDto(saved);
    }

    public AdminLoginResponse adminLogin(@Valid AdminLoginRequest request) {
        // 1. Находим карту админа
        Card cashierCard = cardService.getCardEntityByGuid(request.getCardUuid());

        // 2. Проверяем, что это карта админа (CARD_TYPE_ID = 2)
        if (!Objects.equals(cashierCard.getCardType().getCardTypeId(), tavridaConstants.getCardTypeAdmin())) {
            throw new AuthenticationTavridaException("Карта не принадлежит администратору!");
        }

        // 3. Проверяем пароль и роль
        User user = cashierCard.getUser();
        if (!user.getUserPasswordHash().equals(request.getHashPassword())) {
            throw new AuthenticationTavridaException("Неверный пароль");
        }
        if (!(user.getUserRole().getUserRoleId().equals(tavridaConstants.getUserRoleOperatorFundsAdmin()) ||
                user.getUserRole().getUserRoleId().equals(tavridaConstants.getUserRoleCarrierAdmin()))) {
            throw new AuthenticationTavridaException("Роль у пользователя должна быть - администратор, администратор перевозчика!");
        }

        // 4. Проверяем терминал
        Terminal terminal = terminalService.getTerminalEntityByTerminalGuid(request.getTerminalGuid())
                .orElseThrow(() -> new EntityNotFoundException("Terminal not found: " + request.getTerminalGuid()));

        // 5. Создаём сессию
        UserSession session = new UserSession();
        session.setSessionId(UUID.randomUUID());
        session.setUser(user);
        session.setTerminal(terminal);
        session.setExpirationTime(Instant.now().plus(31, ChronoUnit.DAYS));
        UserSession savedSession = sessionService.save(session);
        return new AdminLoginResponse(savedSession.getSessionId());
    }
}