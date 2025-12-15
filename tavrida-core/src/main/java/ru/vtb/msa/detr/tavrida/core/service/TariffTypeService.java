package ru.vtb.msa.detr.tavrida.core.service;

import ru.vtb.msa.detr.tavrida.api.model.tariff.TariffTypeDto;
import java.util.List;

public interface TariffTypeService {
    TariffTypeDto findById(Integer id);
    TariffTypeDto findByCode(String code);
    List<TariffTypeDto> findAll();
    TariffTypeDto create(TariffTypeDto dto);
    TariffTypeDto update(Integer id, TariffTypeDto dto);
    void delete(Integer id);
}