package ru.vtb.msa.detr.tavrida.core.repo;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.vtb.msa.detr.tavrida.core.model.Card;

import java.util.Optional;
import java.util.UUID;

public interface CardRepository extends JpaRepository<Card, Long> {

    @EntityGraph(attributePaths = {"cardType", "user", "transport", "cardPanHash", "tariffType", "user.userRole", "user.carrier", "transport.carrier"})
    Optional<Card> findByCardGuid(UUID cardGuid);

    @EntityGraph(attributePaths = {"cardType", "user", "transport", "cardPanHash", "tariffType"})
    Optional<Card> findByUser_UserId(Long userId);

    @EntityGraph(attributePaths = {"cardType", "user", "transport", "cardPanHash", "tariffType"})
    Optional<Card> findByCardPanHash_PanId(Long panId);
    @EntityGraph(attributePaths = {"cardType", "user", "transport", "cardPanHash", "tariffType"})
    Optional<Card> findByCardPanHash_panHash(String panHash);

    boolean existsByCardGuid(UUID cardGuid);
}