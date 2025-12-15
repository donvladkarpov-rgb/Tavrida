package ru.vtb.msa.detr.tavrida.core.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.vtb.msa.detr.tavrida.core.model.TransportStop;

import java.util.List;
import java.util.Optional;

public interface TransportStopRepository extends JpaRepository<TransportStop, Long> {
    Optional<TransportStop> findByStopCode(String stopCode);
    List<TransportStop> findByFareZone_ZoneId(Long zoneId);
    List<TransportStop> findByIsActiveTrue();
}