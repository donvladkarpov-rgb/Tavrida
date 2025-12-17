package ru.vtb.msa.detr.tavrida.core.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.vtb.msa.detr.tavrida.core.model.StopsZoneMap;

import java.util.List;
import java.util.Optional;

@Repository
public interface StopsZoneMapRepository extends JpaRepository<StopsZoneMap, Long> {

    List<StopsZoneMap> findByStopId(Long stopId);
    List<StopsZoneMap> findByZoneId(Long zoneId);
    Optional<StopsZoneMap> findByStopIdAndZoneId(Long stopId, Long zoneId);
    boolean existsByStopIdAndZoneId(Long stopId, Long zoneId);
}