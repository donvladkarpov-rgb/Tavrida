// ru/vtb/msa/detr/tavrida/core/service/UserRoleService.java
package ru.vtb.msa.detr.tavrida.core.service;

import org.springframework.stereotype.Service;
import ru.vtb.msa.detr.tavrida.core.model.mapper.TavridaMapper;
import ru.vtb.msa.detr.tavrida.api.model.UserRoleDto;
import ru.vtb.msa.detr.tavrida.core.exception.EntityNotFoundException;
import ru.vtb.msa.detr.tavrida.core.model.UserRole;
import ru.vtb.msa.detr.tavrida.core.repo.UserRoleRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserRoleService {

    private final UserRoleRepository userRoleRepository;

    public UserRoleService(UserRoleRepository userRoleRepository) {
        this.userRoleRepository = userRoleRepository;
    }

    public List<UserRoleDto> getAllRoles() {
        return userRoleRepository.findAll().stream()
                .map(TavridaMapper::toUserRoleDto)
                .collect(Collectors.toList());
    }

    public UserRoleDto getRoleById(Integer id) {
        UserRole role = userRoleRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("User role not found: " + id));
        return TavridaMapper.toUserRoleDto(role);
    }

    public UserRoleDto createRole(UserRoleDto dto) {
        UserRole role = TavridaMapper.toUserRoleEntity(dto);
        UserRole saved = userRoleRepository.save(role);
        return TavridaMapper.toUserRoleDto(saved);
    }

    public UserRoleDto updateRole(Integer id, UserRoleDto dto) {
        UserRole existing = userRoleRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("User role not found: " + id));
        existing.setUserRoleName(dto.getUserRoleName());
        UserRole updated = userRoleRepository.save(existing);
        return TavridaMapper.toUserRoleDto(updated);
    }

    public void deleteRole(Integer id) {
        if (!userRoleRepository.existsById(id)) {
            throw new EntityNotFoundException("User role not found: " + id);
        }
        userRoleRepository.deleteById(id);
    }
}