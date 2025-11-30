package ru.vtb.msa.detr.tavrida.core.service;

import org.springframework.stereotype.Service;
import ru.vtb.msa.detr.tavrida.core.model.mapper.TavridaMapper;
import ru.vtb.msa.detr.tavrida.api.model.CardTypeDto;
import ru.vtb.msa.detr.tavrida.core.exception.EntityNotFoundException;
import ru.vtb.msa.detr.tavrida.core.model.CardType;
import ru.vtb.msa.detr.tavrida.core.repo.CardTypeRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CardTypeService {

    private final CardTypeRepository cardTypeRepository;

    public CardTypeService(CardTypeRepository cardTypeRepository) {
        this.cardTypeRepository = cardTypeRepository;
    }

    public List<CardTypeDto> getAllCardTypes() {
        return cardTypeRepository.findAll().stream()
                .map(TavridaMapper::toCardTypeDto)
                .collect(Collectors.toList());
    }

    public CardTypeDto getCardTypeById(Integer id) {
        CardType type = cardTypeRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Card type not found: " + id));
        return TavridaMapper.toCardTypeDto(type);
    }

    public CardTypeDto createCardType(CardTypeDto dto) {
        CardType type = TavridaMapper.toCardTypeEntity(dto);
        CardType saved = cardTypeRepository.save(type);
        return TavridaMapper.toCardTypeDto(saved);
    }

    public CardTypeDto updateCardType(Integer id, CardTypeDto dto) {
        CardType existing = cardTypeRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Card type not found: " + id));
        existing.setCardTypeName(dto.getCardTypeName());
        CardType updated = cardTypeRepository.save(existing);
        return TavridaMapper.toCardTypeDto(updated);
    }

    public void deleteCardType(Integer id) {
        if (!cardTypeRepository.existsById(id)) {
            throw new EntityNotFoundException("Card type not found: " + id);
        }
        cardTypeRepository.deleteById(id);
    }
}