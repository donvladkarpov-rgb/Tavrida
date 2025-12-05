package ru.vtb.msa.detr.tavrida.core.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.vtb.msa.detr.tavrida.core.model.Transport;

import java.util.Optional;
import java.util.UUID;

public interface TransportRepository extends JpaRepository<Transport, Long> {
    boolean existsByTransportGuid(UUID transportGuid);

    Optional<Transport> findByTransportGuid(UUID transportGuid);

}