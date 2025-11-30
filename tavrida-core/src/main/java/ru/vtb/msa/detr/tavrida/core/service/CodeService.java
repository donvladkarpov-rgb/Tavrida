package ru.vtb.msa.detr.tavrida.core.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.vtb.msa.detr.tavrida.api.model.CodeDto;
import ru.vtb.msa.detr.tavrida.core.exception.EntityNotFoundException;
import ru.vtb.msa.detr.tavrida.core.model.Code;
import ru.vtb.msa.detr.tavrida.core.model.mapper.TavridaMapper;
import ru.vtb.msa.detr.tavrida.core.repo.CodeRepository;

@Service
@Transactional
public class CodeService {

    private final CodeRepository codeRepository;

    public CodeService(CodeRepository codeRepository) {
        this.codeRepository = codeRepository;
    }

    public CodeDto createCode(CodeDto dto) {
        if (codeRepository.existsByCodeUid(dto.getCodeUid())) {
            throw new IllegalArgumentException("Code UID already exists: " + dto.getCodeUid());
        }
        Code code = TavridaMapper.toCodeEntity(dto);
        Code saved = codeRepository.save(code);
        return TavridaMapper.toCodeDto(saved);
    }

    public CodeDto getCode(String codeUid) {
        Code code = codeRepository.findById(codeUid)
                .orElseThrow(() -> new EntityNotFoundException("Code not found: " + codeUid));
        return TavridaMapper.toCodeDto(code);
    }

    public CodeDto updateCode(String codeUid, CodeDto dto) {
        Code existing = codeRepository.findById(codeUid)
                .orElseThrow(() -> new EntityNotFoundException("Code not found: " + codeUid));
        existing.setCodeType(dto.getCodeType());
        existing.setCodeContent(dto.getCodeContent());
        existing.setAllowedUsage(dto.getAllowedUsage());
        existing.setValidFrom(dto.getValidFrom());
        existing.setValidTo(dto.getValidTo());
        Code updated = codeRepository.save(existing);
        return TavridaMapper.toCodeDto(updated);
    }

    public void deleteCode(String codeUid) {
        if (!codeRepository.existsByCodeUid(codeUid)) {
            throw new EntityNotFoundException("Code not found: " + codeUid);
        }
        codeRepository.deleteById(codeUid);
    }
}