package ru.vtb.msa.detr.tavrida.core.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.vtb.msa.detr.tavrida.core.model.BlackList;

import java.util.UUID;

public interface BlackListRepository extends JpaRepository<BlackList, UUID> {
    boolean existsByCardGuid(UUID cardGuid);
}