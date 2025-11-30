package ru.vtb.msa.detr.tavrida.core.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.vtb.msa.detr.tavrida.core.model.Code;

public interface CodeRepository extends JpaRepository<Code, String> {
    boolean existsByCodeUid(String codeUid);
}