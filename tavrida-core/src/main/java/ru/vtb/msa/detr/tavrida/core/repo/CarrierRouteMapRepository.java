package ru.vtb.msa.detr.tavrida.core.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.vtb.msa.detr.tavrida.core.model.CarrierRouteMap;

import java.util.List;

public interface CarrierRouteMapRepository extends JpaRepository<CarrierRouteMap, Long> {
    List<CarrierRouteMap> findByCarrier_CarrierId(Long carrierId);
    List<CarrierRouteMap> findByRoute_RouteId(Long routeId);

    boolean existsByCarrier_CarrierIdAndRoute_RouteId(Long carrierId, Long routeId);
}