package ru.vtb.msa.detr.tavrida.core.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.vtb.msa.detr.tavrida.core.model.StopsZoneMap;

import java.util.List;
import java.util.Optional;

@Repository
public interface StopsZoneMapRepository extends JpaRepository<StopsZoneMap, Long> {

    List<StopsZoneMap> findByStop_StopId(Long stopId);
    List<StopsZoneMap> findByZone_ZoneId(Long zoneId);
    Optional<StopsZoneMap> findByStop_StopIdAndZone_ZoneId(Long stopId, Long zoneId);
    boolean existsByStop_StopIdAndZone_ZoneId(Long stopId, Long zoneId);
}