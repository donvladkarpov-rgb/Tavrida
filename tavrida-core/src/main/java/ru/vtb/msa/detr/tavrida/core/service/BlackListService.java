package ru.vtb.msa.detr.tavrida.core.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.vtb.msa.detr.tavrida.core.model.mapper.TavridaMapper;
import ru.vtb.msa.detr.tavrida.api.model.BlackListDto;
import ru.vtb.msa.detr.tavrida.core.exception.EntityNotFoundException;
import ru.vtb.msa.detr.tavrida.core.model.BlackList;
import ru.vtb.msa.detr.tavrida.core.repo.BlackListRepository;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Transactional
public class BlackListService {

    private final BlackListRepository blackListRepository;

    public BlackListService(BlackListRepository blackListRepository) {
        this.blackListRepository = blackListRepository;
    }

    public List<BlackListDto> getAllBlackListEntries() {
        return blackListRepository.findAll().stream()
                .map(TavridaMapper::toBlackListDto)
                .collect(Collectors.toList());
    }

    public BlackListDto getBlackListEntryById(UUID id) {
        BlackList entry = blackListRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Black list entry not found: " + id));
        return TavridaMapper.toBlackListDto(entry);
    }

    public BlackListDto addCardToBlackList(BlackListDto dto) {
        BlackList entry = TavridaMapper.toBlackListEntity(dto);
        BlackList saved = blackListRepository.save(entry);
        return TavridaMapper.toBlackListDto(saved);
    }

    public void removeCardFromBlackList(UUID id) {
        if (!blackListRepository.existsById(id)) {
            throw new EntityNotFoundException("Black list entry not found: " + id);
        }
        blackListRepository.deleteById(id);
    }

    public boolean isCardBlocked(java.util.UUID cardGuid) {
        return blackListRepository.existsByCardGuid(cardGuid);
    }
}