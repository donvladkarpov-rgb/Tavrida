package ru.vtb.msa.detr.tavrida.core.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.vtb.msa.detr.tavrida.core.model.FareZone;

import java.util.Optional;

public interface FareZoneRepository extends JpaRepository<FareZone, Long> {
    Optional<FareZone> findByZoneCode(String zoneCode);
    Optional<FareZone> findByZoneName(String zoneName);
}