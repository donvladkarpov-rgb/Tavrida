package ru.vtb.msa.detr.tavrida.core.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.vtb.msa.detr.tavrida.core.model.Carrier;

public interface CarrierRepository extends JpaRepository<Carrier, Long> {
    // JpaRepository предоставляет все стандартные CRUD-операции:
    // - findAll(), findById(id), save(entity), delete(entity), existsById(id)
    // - и методы с пейджингом/сортировкой
}