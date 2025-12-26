package ru.vtb.msa.detr.tavrida.core.model.mapper;

import ru.vtb.msa.detr.tavrida.api.model.CardPanHashDto;
import ru.vtb.msa.detr.tavrida.core.model.CardPanHash;

import java.util.List;
import java.util.stream.Collectors;

public class CardPanHashMapper {

    private CardPanHashMapper() {
        // utility class
    }

    public static CardPanHashDto toDto(CardPanHash entity) {
        if (entity == null) {
            return null;
        }
        return new CardPanHashDto(
                entity.getPanId(),
                entity.getPanHash(),
                TavridaMapper.toCardDtoWithUserIdList(entity.getCards())
        );
    }

    public static List<CardPanHashDto> toDtoList(List<CardPanHash> entities) {
        if (entities == null) {
            return List.of();
        }
        return entities.stream()
                .map(CardPanHashMapper::toDto)
                .collect(Collectors.toList());
    }

    // Обратное преобразование (если нужно для создания/обновления)
    public static CardPanHash toEntity(CardPanHashDto dto) {
        if (dto == null) {
            return null;
        }
        CardPanHash entity = new CardPanHash();
        entity.setPanId(dto.getPanId());
        entity.setPanHash(dto.getPanHash());
        return entity;
    }
}