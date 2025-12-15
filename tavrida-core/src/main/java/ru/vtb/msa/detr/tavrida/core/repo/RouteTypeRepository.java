package ru.vtb.msa.detr.tavrida.core.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.vtb.msa.detr.tavrida.core.model.RouteType;

import java.util.Optional;

public interface RouteTypeRepository extends JpaRepository<RouteType, Long> {
    Optional<RouteType> findByRouteTypesName(String name);
}