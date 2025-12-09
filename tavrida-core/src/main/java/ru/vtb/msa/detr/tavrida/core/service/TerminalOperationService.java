package ru.vtb.msa.detr.tavrida.core.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.vtb.msa.detr.tavrida.api.model.terminal.*;
import ru.vtb.msa.detr.tavrida.core.config.TavridaConstants;
import ru.vtb.msa.detr.tavrida.core.exception.EntityNotFoundException;
import ru.vtb.msa.detr.tavrida.core.exception.ValidationTavridaException;
import ru.vtb.msa.detr.tavrida.core.model.*;
import ru.vtb.msa.detr.tavrida.core.repo.*;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class TerminalOperationService {

    private final Random random = new Random();
    private final CardRepository cardRepository;
    private final BlackListRepository blackListRepository;
    private final PaymentRepository paymentRepository;
    private final TerminalRepository terminalRepository;
    private final TransportRepository transportRepository;
    private final TavridaConstants tavridaConstants;
    private final UserRepository userRepository;
    private final UserSessionRepository userSessionRepository;
    private final SessionService sessionService;

    public TerminalOperationService(
            SessionService sessionService,
            CardRepository cardRepository,
            BlackListRepository blackListRepository,
            PaymentRepository paymentRepository,
            TerminalRepository terminalRepository,
            TransportRepository transportRepository,
            TavridaConstants tavridaConstants,
            UserRepository userRepository,
            UserSessionRepository userSessionRepository) {
        this.cardRepository = cardRepository;
        this.blackListRepository = blackListRepository;
        this.paymentRepository = paymentRepository;
        this.terminalRepository = terminalRepository;
        this.transportRepository = transportRepository;
        this.tavridaConstants = tavridaConstants;
        this.userRepository = userRepository;
        this.userSessionRepository = userSessionRepository;
        this.sessionService = sessionService;
    }

    @Transactional
    public DriverSessionResponse startDriverSession(DriverSessionStartRequest request) {
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
        session.setExpirationTime(Instant.now().plus(31, ChronoUnit.DAYS));
        userSessionRepository.save(session);

        // 9. Возвращаем ответ — транспорт берём из терминала (или из request, они совпадают)
        LocalDateTime now = LocalDateTime.now();
        return new DriverSessionResponse(
                true,
                "Сессия водителя успешно начата",
                sessionId,
                request.getDriverCardGuid(),
                request.getTransportGuid(), // или terminal.getTransport().getTransportGuid()
                now
        );
    }

    @Transactional
    public TerminalActivationResponse activateTerminal(TerminalActivationRequest request) {
        if (request.getTerminalGuid() == null) {
            request.setTerminalGuid(UUID.randomUUID());
            request.setTerminalNumber(generateCode());
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
                            "Терминал уже привязан к транспорту ID " + transport.getTransportId(), terminal.getTerminalId());
                }
            }
        } else {
            terminal = new Terminal();
            terminal.setTerminalGuid(request.getTerminalGuid());
            terminal.setTerminalNumber(request.getTerminalNumber());
            terminal.setTerminalSerialNumber(request.getTerminalNumber());
        }
        terminal.setTransport(transport);

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

        return new TerminalActivationResponse(true,
                "Терминал успешно привязан к транспорту ID " + transport.getTransportId(),
                terminal.getTerminalId(),
                terminal.getTerminalGuid(),
                terminal.getTerminalNumber(),
                terminal.getTerminalSerialNumber());
    }

    private String generateCode() {
        // Случайная заглавная буква от 'A' до 'Z'
        char letter = (char) ('A' + random.nextInt(26));
        // Случайное 4-значное число от 0000 до 9999
        int number = random.nextInt(10000);
        // Форматируем число с ведущими нулями
        return String.format("%c%04d", letter, number);
    }

    @Transactional
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

        return new TerminalActivationResponse(true,
                "Терминал успешно отвязан от транспорта", terminal.getTerminalId(),
                request.getTerminalGuid());
    }

    /**
     * Списание поездки с карты
     */
    @Transactional
    public boolean deductTrip(TerminalDeductRequest request) {
        sessionService.getSession(request.getSessionId());

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

        return true;
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

}