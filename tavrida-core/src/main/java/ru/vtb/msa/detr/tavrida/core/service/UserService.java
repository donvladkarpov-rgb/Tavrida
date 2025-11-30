package ru.vtb.msa.detr.tavrida.core.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.vtb.msa.detr.tavrida.core.model.mapper.TavridaMapper;
import ru.vtb.msa.detr.tavrida.api.model.UserDto;
import ru.vtb.msa.detr.tavrida.core.exception.EntityNotFoundException;
import ru.vtb.msa.detr.tavrida.core.model.User;
import ru.vtb.msa.detr.tavrida.core.repo.UserRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public List<UserDto> getAllUsers() {
        return userRepository.findAll().stream()
                .map(TavridaMapper::toUserDto)
                .collect(Collectors.toList());
    }

    public UserDto getUserById(Long id) {
        User user = userRepository.findWithDetailsByUserId(id)
                .orElseThrow(() -> new EntityNotFoundException("User not found: " + id));
        return TavridaMapper.toUserDtoWithCards(user);
    }

    public UserDto createUser(UserDto dto) {
        User user = TavridaMapper.toUserEntity(dto);
        User saved = userRepository.save(user);
        return TavridaMapper.toUserDto(saved);
    }

    public UserDto updateUser(Long id, UserDto dto) {
        User existing = userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("User not found: " + id));
        existing.setUserFio(dto.getUserFio());
        // Обновляем role и carrier
        existing.setUserRole(TavridaMapper.toUserRoleEntity(dto.getUserRole()));
        existing.setCarrier(TavridaMapper.toCarrierEntity(dto.getCarrier()));
        User updated = userRepository.save(existing);
        return TavridaMapper.toUserDto(updated);
    }

    public void deleteUser(Long id) {
        if (!userRepository.existsById(id)) {
            throw new EntityNotFoundException("User not found: " + id);
        }
        userRepository.deleteById(id);
    }
}