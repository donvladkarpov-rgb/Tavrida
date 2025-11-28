package ru.vtb.msa.detr.tavrida.core.repo;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.vtb.msa.detr.tavrida.core.model.User;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    @EntityGraph(attributePaths = {"userRole", "carrier", "cards"})
    Optional<User> findWithDetailsByUserId(Long userId);

    @EntityGraph(attributePaths = {"userRole", "carrier"})
    Optional<User> findByUserFio(String fio);
}