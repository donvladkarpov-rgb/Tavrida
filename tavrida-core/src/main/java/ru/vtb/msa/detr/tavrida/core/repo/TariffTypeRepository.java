package ru.vtb.msa.detr.tavrida.core.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.vtb.msa.detr.tavrida.core.model.TariffType;

import java.util.Optional;

public interface TariffTypeRepository extends JpaRepository<TariffType, Integer> {
    Optional<TariffType> findByCode(String code);
}