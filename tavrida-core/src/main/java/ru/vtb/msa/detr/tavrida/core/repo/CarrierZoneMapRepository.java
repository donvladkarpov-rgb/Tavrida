package ru.vtb.msa.detr.tavrida.core.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.vtb.msa.detr.tavrida.core.model.CarrierZoneMap;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public interface CarrierZoneMapRepository extends JpaRepository<CarrierZoneMap, Long> {
    Optional<CarrierZoneMap> findByCarrier_CarrierIdAndFareZone_ZoneId(Long carrierId, Long zoneId);
    List<CarrierZoneMap> findByCarrier_CarrierId(Long carrierId);
    List<CarrierZoneMap> findByFareZone_ZoneId(Long zoneId);
}