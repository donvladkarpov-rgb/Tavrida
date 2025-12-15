package ru.vtb.msa.detr.tavrida.core.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.vtb.msa.detr.tavrida.core.model.Route;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface RouteRepository extends JpaRepository<Route, Long> {
    Optional<Route> findByRouteGuid(UUID routeGuid);
    List<Route> findByParentRouteIsNull(); // корневые маршруты
    List<Route> findByParentRoute_RouteId(Long parentRouteId);
}