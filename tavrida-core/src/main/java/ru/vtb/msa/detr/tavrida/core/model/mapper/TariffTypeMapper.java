package ru.vtb.msa.detr.tavrida.core.model.mapper;

import ru.vtb.msa.detr.tavrida.api.model.tariff.TariffTypeDto;
import ru.vtb.msa.detr.tavrida.core.model.TariffType;

public class TariffTypeMapper {
    public static TariffTypeDto toDto(TariffType entity) {
        if (entity == null) return null;
        return new TariffTypeDto(
                entity.getTariffTypeId(),
                entity.getCode(),
                entity.getName(),
                entity.getDescription()
        );
    }

    public static TariffType toEntity(TariffTypeDto dto) {
        if (dto == null) return null;
        TariffType entity = new TariffType();
        entity.setTariffTypeId(dto.getTariffTypeId());
        entity.setCode(dto.getCode());
        entity.setName(dto.getName());
        entity.setDescription(dto.getDescription());
        return entity;
    }
}