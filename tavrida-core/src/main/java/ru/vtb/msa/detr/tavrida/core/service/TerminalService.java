package ru.vtb.msa.detr.tavrida.core.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.vtb.msa.detr.tavrida.core.model.mapper.TavridaMapper;
import ru.vtb.msa.detr.tavrida.api.model.TerminalDto;
import ru.vtb.msa.detr.tavrida.core.exception.EntityNotFoundException;
import ru.vtb.msa.detr.tavrida.core.model.Terminal;
import ru.vtb.msa.detr.tavrida.core.repo.TerminalRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class TerminalService {

    private final TerminalRepository terminalRepository;

    public TerminalService(TerminalRepository terminalRepository) {
        this.terminalRepository = terminalRepository;
    }

    public List<TerminalDto> getAllTerminals() {
        return terminalRepository.findAll().stream()
                .map(TavridaMapper::toTerminalDto)
                .collect(Collectors.toList());
    }

    public TerminalDto getTerminalById(Long id) {
        Terminal terminal = terminalRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Terminal not found: " + id));
        return TavridaMapper.toTerminalDto(terminal);
    }

    public TerminalDto createTerminal(TerminalDto dto) {
        Terminal terminal = TavridaMapper.toTerminalEntity(dto);
        Terminal saved = terminalRepository.save(terminal);
        return TavridaMapper.toTerminalDto(saved);
    }

    public TerminalDto updateTerminal(Long id, TerminalDto dto) {
        Terminal existing = terminalRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Terminal not found: " + id));
        existing.setTransport(TavridaMapper.toTransportEntity(dto.getTransport()));
        existing.setTerminalGuid(dto.getTerminalGuid());
        existing.setTerminalNumber(dto.getTerminalNumber());
        Terminal updated = terminalRepository.save(existing);
        return TavridaMapper.toTerminalDto(updated);
    }

    public void deleteTerminal(Long id) {
        if (!terminalRepository.existsById(id)) {
            throw new EntityNotFoundException("Terminal not found: " + id);
        }
        terminalRepository.deleteById(id);
    }
}