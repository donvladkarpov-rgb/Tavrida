package ru.vtb.msa.detr.tavrida.core.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.vtb.msa.detr.tavrida.core.model.UserRole;

public interface UserRoleRepository extends JpaRepository<UserRole, Integer> {
    // JpaRepository даёт: findAll(), findById(), save(), delete(), existsById() и т.д.
}