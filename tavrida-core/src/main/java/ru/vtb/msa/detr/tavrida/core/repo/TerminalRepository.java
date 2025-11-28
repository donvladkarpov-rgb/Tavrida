package ru.vtb.msa.detr.tavrida.core.repo;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.vtb.msa.detr.tavrida.core.model.Terminal;

import java.util.Optional;
import java.util.UUID;

public interface TerminalRepository extends JpaRepository<Terminal, Long> {

    @EntityGraph(attributePaths = {"transport", "transport.carrier"})
    Optional<Terminal> findByTerminalGuid(UUID terminalGuid);
}