package ru.vtb.msa.detr.tavrida.core.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.vtb.msa.detr.tavrida.api.model.CardPanHashDto;
import ru.vtb.msa.detr.tavrida.core.exception.EntityNotFoundException;
import ru.vtb.msa.detr.tavrida.core.model.mapper.CardPanHashMapper;
import ru.vtb.msa.detr.tavrida.core.model.CardPanHash;
import ru.vtb.msa.detr.tavrida.core.repo.CardPanHashRepository;

import java.util.List;

@Service
public class CardPanHashService {

    private final CardPanHashRepository repository;

    public CardPanHashService(CardPanHashRepository repository) {
        this.repository = repository;
    }

    @Transactional(readOnly = true)
    public CardPanHashDto findById(Long id) {
        CardPanHash entity = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("CardPanHash not found with id: " + id));
        return CardPanHashMapper.toDto(entity);
    }

    @Transactional(readOnly = true)
    public CardPanHashDto findByPanHash(String panHash) {
        CardPanHash entity = repository.findByPanHash(panHash)
                .orElseThrow(() -> new EntityNotFoundException("CardPanHash not found with hash: " + panHash));
        return CardPanHashMapper.toDto(entity);
    }

    @Transactional(readOnly = true)
    public List<CardPanHashDto> findAll() {
        return CardPanHashMapper.toDtoList(repository.findAll());
    }

    @Transactional
    public CardPanHashDto create(CardPanHashDto dto) {
        CardPanHash entity = CardPanHashMapper.toEntity(dto);
        CardPanHash saved = repository.save(entity);
        return CardPanHashMapper.toDto(saved);
    }

    @Transactional
    public CardPanHashDto update(Long id, CardPanHashDto dto) {
        if (!repository.existsById(id)) {
            throw new EntityNotFoundException("CardPanHash not found with id: " + id);
        }
        CardPanHash entity = CardPanHashMapper.toEntity(dto);
        entity.setPanId(id); // важно: сохраняем ID
        CardPanHash updated = repository.save(entity);
        return CardPanHashMapper.toDto(updated);
    }

    @Transactional
    public void deleteById(Long id) {
        if (!repository.existsById(id)) {
            throw new EntityNotFoundException("CardPanHash not found with id: " + id);
        }
        repository.deleteById(id);
    }
}