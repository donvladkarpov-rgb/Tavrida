package ru.vtb.msa.detr.tavrida.core.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.vtb.msa.detr.tavrida.api.model.UserSessionDto;
import ru.vtb.msa.detr.tavrida.core.exception.EntityNotFoundException;
import ru.vtb.msa.detr.tavrida.core.model.mapper.TavridaMapper;
import ru.vtb.msa.detr.tavrida.core.model.UserSession;
import ru.vtb.msa.detr.tavrida.core.repo.UserSessionRepository;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@Transactional
public class SessionService {

    private final UserSessionRepository sessionRepository;

    public SessionService(UserSessionRepository sessionRepository) {
        this.sessionRepository = sessionRepository;
    }

    public UserSessionDto createSession(UserSessionDto dto) {
        UserSession session = TavridaMapper.toUserSessionEntity(dto);
        UserSession saved = sessionRepository.save(session);
        return TavridaMapper.toUserSessionDto(saved);
    }

    public UserSessionDto getSession(UUID sessionId) {
        UserSession session = sessionRepository.findById(sessionId)
                .orElseThrow(() -> new EntityNotFoundException("Session not found: " + sessionId));
        return TavridaMapper.toUserSessionDto(session);
    }

    public void deleteExpiredSessions() {
        sessionRepository.deleteByExpirationTimeBefore(LocalDateTime.now());
    }

    public boolean isValid(UUID sessionId) {
        return sessionRepository.existsBySessionId(sessionId);
    }
}