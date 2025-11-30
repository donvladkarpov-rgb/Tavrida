package ru.vtb.msa.detr.tavrida.core.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.vtb.msa.detr.tavrida.core.model.UserSession;
import java.util.UUID;

public interface UserSessionRepository extends JpaRepository<UserSession, UUID> {
    boolean existsBySessionId(UUID sessionId);
    void deleteByExpirationTimeBefore(java.time.LocalDateTime now);
}