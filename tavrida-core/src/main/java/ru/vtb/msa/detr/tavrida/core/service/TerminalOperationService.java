package ru.vtb.msa.detr.tavrida.core.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.vtb.msa.detr.tavrida.api.model.terminal.TerminalDeductRequest;
import ru.vtb.msa.detr.tavrida.core.exception.EntityNotFoundException;
import ru.vtb.msa.detr.tavrida.core.exception.ValidationTavridaException;
import ru.vtb.msa.detr.tavrida.core.model.BlackList;
import ru.vtb.msa.detr.tavrida.core.model.Card;
import ru.vtb.msa.detr.tavrida.core.repo.BlackListRepository;
import ru.vtb.msa.detr.tavrida.core.repo.CardRepository;
import ru.vtb.msa.detr.tavrida.core.repo.PaymentRepository;
import ru.vtb.msa.detr.tavrida.core.repo.TerminalRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class TerminalOperationService {

    private final CardRepository cardRepository;
    private final BlackListRepository blackListRepository;
    private final PaymentRepository paymentRepository;
    private final TerminalRepository terminalRepository;

    public TerminalOperationService(
            CardRepository cardRepository,
            BlackListRepository blackListRepository,
            PaymentRepository paymentRepository,
            TerminalRepository terminalRepository) {
        this.cardRepository = cardRepository;
        this.blackListRepository = blackListRepository;
        this.paymentRepository = paymentRepository;
        this.terminalRepository = terminalRepository;
    }

    /**
     * Списание поездки с карты
     */
    @Transactional
    public boolean deductTrip(TerminalDeductRequest request) {
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