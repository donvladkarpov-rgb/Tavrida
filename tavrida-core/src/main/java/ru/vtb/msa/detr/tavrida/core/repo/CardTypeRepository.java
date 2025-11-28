package ru.vtb.msa.detr.tavrida.core.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.vtb.msa.detr.tavrida.core.model.CardType;

public interface CardTypeRepository extends JpaRepository<CardType, Integer> {
    // JpaRepository предоставляет:
    // - findAll(), findById(), save(), delete(), existsById()
    // - и другие стандартные методы CRUD
}