package ru.vtb.msa.detr.tavrida.core.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.vtb.msa.detr.tavrida.core.model.StopsRouteMap;

import java.util.List;

public interface StopsRouteMapRepository extends JpaRepository<StopsRouteMap, Long> {
    List<StopsRouteMap> findByRoute_RouteIdOrderBySerialNumberAsc(Long routeId);
    List<StopsRouteMap> findByStop_StopId(Long stopId);
}