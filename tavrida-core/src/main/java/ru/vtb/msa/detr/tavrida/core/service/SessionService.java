package ru.vtb.msa.detr.tavrida.core.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.vtb.msa.detr.tavrida.api.model.UserSessionDto;
import ru.vtb.msa.detr.tavrida.core.exception.EntityNotFoundException;
import ru.vtb.msa.detr.tavrida.core.model.Terminal;
import ru.vtb.msa.detr.tavrida.core.model.User;
import ru.vtb.msa.detr.tavrida.core.model.mapper.TavridaMapper;
import ru.vtb.msa.detr.tavrida.core.model.UserSession;
import ru.vtb.msa.detr.tavrida.core.repo.TerminalRepository;
import ru.vtb.msa.detr.tavrida.core.repo.UserRepository;
import ru.vtb.msa.detr.tavrida.core.repo.UserSessionRepository;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@Transactional
public class SessionService {

    private final UserSessionRepository sessionRepository;
    TerminalRepository terminalRepository;
    UserRepository userRepository;

    public SessionService(
            UserSessionRepository sessionRepository,
            TerminalRepository terminalRepository,
            UserRepository userRepository
    ) {
        this.sessionRepository = sessionRepository;
        this.terminalRepository = terminalRepository;
        this.userRepository = userRepository;
    }

    public UserSessionDto createSession(UserSessionDto dto) {
        Terminal terminal = terminalRepository.findById(dto.getTerminal().getTerminalId()).orElse(null);
        User user = userRepository.findById(dto.getUser().getUserId()).orElse(null);
        UserSession session = TavridaMapper.toUserSessionEntity(dto, user, terminal);
        UserSession saved = sessionRepository.save(session);
        return TavridaMapper.toUserSessionDto(saved);
    }

    public UserSessionDto getSession(UUID sessionId) {
        UserSession session = sessionRepository.findById(sessionId)
                .orElseThrow(() -> new EntityNotFoundException("Session not found: " + sessionId));
        return TavridaMapper.toUserSessionDto(session);
    }

    public UserSession getSessionEntity(UUID sessionId) {
        UserSession session = sessionRepository.findById(sessionId)
                .orElseThrow(() -> new EntityNotFoundException("Session not found: " + sessionId));
        return session;
    }

    public void deleteExpiredSessions() {
        sessionRepository.deleteByExpirationTimeBefore(LocalDateTime.now());
    }

    public boolean isValid(UUID sessionId) {
        return sessionRepository.existsBySessionId(sessionId);
    }

    public UserSession save(UserSession session) {
        return sessionRepository.save(session);
    }
}