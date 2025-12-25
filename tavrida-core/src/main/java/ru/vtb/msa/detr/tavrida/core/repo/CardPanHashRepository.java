package ru.vtb.msa.detr.tavrida.core.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.vtb.msa.detr.tavrida.core.model.CardPanHash;

import java.util.Optional;

@Repository
public interface CardPanHashRepository extends JpaRepository<CardPanHash, Long> {
    Optional<CardPanHash> findByPanHash(String panHash);
}