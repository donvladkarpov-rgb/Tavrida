package ru.vtb.msa.detr.tavrida.core.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.vtb.msa.detr.tavrida.core.model.Trip;

import java.util.List;
import java.util.UUID;

public interface TripRepository extends JpaRepository<Trip, Long> {
    List<Trip> findByRoute_RouteId(Long routeId);
    List<Trip> findBySession_SessionId(UUID sessionId);
}