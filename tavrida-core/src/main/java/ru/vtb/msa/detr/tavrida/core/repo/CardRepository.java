package ru.vtb.msa.detr.tavrida.core.repo;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.vtb.msa.detr.tavrida.core.model.Card;

import java.util.Optional;
import java.util.UUID;

public interface CardRepository extends JpaRepository<Card, Long> {

    @EntityGraph(attributePaths = {"cardType", "user", "user.userRole", "user.carrier"})
    Optional<Card> findByCardGuid(UUID cardGuid);

    @EntityGraph(attributePaths = {"cardType", "user"})
    Optional<Card> findByUserUserId(Long userId);

    boolean existsByCardGuid(UUID cardGuid);
}