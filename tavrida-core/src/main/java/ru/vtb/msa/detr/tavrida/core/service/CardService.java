package ru.vtb.msa.detr.tavrida.core.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.vtb.msa.detr.tavrida.core.model.mapper.TavridaMapper;
import ru.vtb.msa.detr.tavrida.api.model.CardDto;
import ru.vtb.msa.detr.tavrida.core.exception.EntityNotFoundException;
import ru.vtb.msa.detr.tavrida.core.model.Card;
import ru.vtb.msa.detr.tavrida.core.repo.CardRepository;
import ru.vtb.msa.detr.tavrida.core.repo.UserRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class CardService {

    private final CardRepository cardRepository;
    private final UserRepository userRepository;

    public CardService(CardRepository cardRepository, UserRepository userRepository) {
        this.cardRepository = cardRepository;
        this.userRepository = userRepository;
    }

    public List<CardDto> getAllCards() {
        return cardRepository.findAll().stream()
                .map(TavridaMapper::toCardDtoWithUserId)
                .collect(Collectors.toList());
    }

    public CardDto getCardById(Long id) {
        Card card = cardRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Card not found: " + id));
        return TavridaMapper.toCardDtoWithUserId(card);
    }

    public CardDto getCardByGuid(java.util.UUID guid) {
        Card card = cardRepository.findByCardGuid(guid)
                .orElseThrow(() -> new EntityNotFoundException("Card not found: " + guid));
        return TavridaMapper.toCardDtoWithUserId(card);
    }

    public CardDto createCard(CardDto dto) {
        Card card = TavridaMapper.toCardEntity(dto);
        // Устанавливаем пользователя, если указан
        if (dto.getUser() != null && dto.getUser().getUserId() != null) {
            var user = userRepository.findById(dto.getUser().getUserId())
                    .orElseThrow(() -> new EntityNotFoundException("User not found: " + dto.getUser().getUserId()));
            card.setUser(user);
        }
        Card saved = cardRepository.save(card);
        return TavridaMapper.toCardDtoWithUserId(saved);
    }

    public CardDto updateCard(Long id, CardDto dto) {
        Card existing = cardRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Card not found: " + id));
        existing.setCardGuid(dto.getCardGuid());
        existing.setCardType(TavridaMapper.toCardTypeEntity(dto.getCardType()));
        if (dto.getUser() != null && dto.getUser().getUserId() != null) {
            var user = userRepository.findById(dto.getUser().getUserId())
                    .orElseThrow(() -> new EntityNotFoundException("User not found: " + dto.getUser().getUserId()));
            existing.setUser(user);
        }
        existing.setUniqueTravelCount(dto.getUniqueTravelCount());
        existing.setMaximumUniqueCount(dto.getMaximumUniqueCount());
        existing.setAvailableTravelCount(dto.getAvailableTravelCount());
        existing.setExpirationDate(dto.getExpirationDate());
        Card updated = cardRepository.save(existing);
        return TavridaMapper.toCardDtoWithUserId(updated);
    }

    public void deleteCard(Long id) {
        if (!cardRepository.existsById(id)) {
            throw new EntityNotFoundException("Card not found: " + id);
        }
        cardRepository.deleteById(id);
    }
}