package ru.vtb.msa.detr.tavrida.core.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.vtb.msa.detr.tavrida.api.model.PaymentDto;
import ru.vtb.msa.detr.tavrida.core.model.Payment;
import ru.vtb.msa.detr.tavrida.core.model.mapper.TavridaMapper;
import ru.vtb.msa.detr.tavrida.core.repo.PaymentRepository;

import java.util.List;

@Service
@Transactional
public class PaymentService {

    private final PaymentRepository paymentRepository;

    public PaymentService(PaymentRepository paymentRepository) {
        this.paymentRepository = paymentRepository;
    }

    public PaymentDto createPayment(PaymentDto dto) {
        Payment payment = TavridaMapper.toPaymentEntity(dto);
        Payment saved = paymentRepository.save(payment);
        return TavridaMapper.toPaymentDto(saved);
    }

    public List<PaymentDto> getPaymentsByCard(Long cardId) {
        return paymentRepository.findByCardId(cardId).stream()
                .map(TavridaMapper::toPaymentDto)
                .toList();
    }

    public List<PaymentDto> getPaymentsByTerminal(Long terminalId) {
        return paymentRepository.findByTerminalId(terminalId).stream()
                .map(TavridaMapper::toPaymentDto)
                .toList();
    }

    public List<PaymentDto> getPaymentsByType(Integer paymentTypeId) {
        return paymentRepository.findByPaymentType_PaymentTypeId(paymentTypeId).stream()
                .map(TavridaMapper::toPaymentDto)
                .toList();
    }

    public List<PaymentDto> getPaymentsByResult(Integer paymentResultId) {
        return paymentRepository.findByPaymentResult_PaymentResultId(paymentResultId).stream()
                .map(TavridaMapper::toPaymentDto)
                .toList();
    }
}