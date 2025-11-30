package ru.vtb.msa.detr.tavrida.core.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.vtb.msa.detr.tavrida.core.model.ServiceEvent;
import java.util.List;

public interface ServiceEventRepository extends JpaRepository<ServiceEvent, Long> {
    List<ServiceEvent> findByDoerUserId(Long userId);
    List<ServiceEvent> findByReferenceId(Long referenceId);
}