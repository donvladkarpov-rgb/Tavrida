package ru.vtb.msa.detr.tavrida.core.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.vtb.msa.detr.tavrida.core.model.Payment;
import java.util.List;

public interface PaymentRepository extends JpaRepository<Payment, Long> {
    List<Payment> findByCardId(Long cardId);
    List<Payment> findByTerminalId(Long terminalId);
    List<Payment> findByPaymentType_PaymentTypeId(Integer paymentTypeId);
    List<Payment> findByPaymentResult_PaymentResultId(Integer paymentResultId);
}