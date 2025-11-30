package ru.vtb.msa.detr.tavrida.core.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.vtb.msa.detr.tavrida.core.model.MasterPassword;

public interface MasterPasswordRepository extends JpaRepository<MasterPassword, Long> {
    // Может быть только одна запись
}