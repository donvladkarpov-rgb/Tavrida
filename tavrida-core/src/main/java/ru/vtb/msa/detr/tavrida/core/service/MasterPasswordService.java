package ru.vtb.msa.detr.tavrida.core.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.vtb.msa.detr.tavrida.api.model.MasterPasswordDto;
import ru.vtb.msa.detr.tavrida.core.model.MasterPassword;
import ru.vtb.msa.detr.tavrida.core.model.mapper.TavridaMapper;
import ru.vtb.msa.detr.tavrida.core.repo.MasterPasswordRepository;

@Service
@Transactional
public class MasterPasswordService {

    private final MasterPasswordRepository masterPasswordRepository;

    public MasterPasswordService(MasterPasswordRepository masterPasswordRepository) {
        this.masterPasswordRepository = masterPasswordRepository;
    }

    public MasterPasswordDto setMasterPassword(MasterPasswordDto dto) {
        // Удаляем старый, если есть
        masterPasswordRepository.deleteAll();
        MasterPassword mp = TavridaMapper.toMasterPasswordEntity(dto);
        MasterPassword saved = masterPasswordRepository.save(mp);
        return TavridaMapper.toMasterPasswordDto(saved);
    }

    public MasterPasswordDto getMasterPassword() {
        return masterPasswordRepository.findAll().stream()
                .findFirst()
                .map(TavridaMapper::toMasterPasswordDto)
                .orElse(null);
    }
}