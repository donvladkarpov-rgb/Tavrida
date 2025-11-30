package ru.vtb.msa.detr.tavrida.core.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.vtb.msa.detr.tavrida.api.model.PaymentResultDto;
import ru.vtb.msa.detr.tavrida.core.exception.EntityNotFoundException;
import ru.vtb.msa.detr.tavrida.core.model.PaymentResult;
import ru.vtb.msa.detr.tavrida.core.model.mapper.TavridaMapper;
import ru.vtb.msa.detr.tavrida.core.repo.PaymentResultRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class PaymentResultService {

    private final PaymentResultRepository paymentResultRepository;

    public PaymentResultService(PaymentResultRepository paymentResultRepository) {
        this.paymentResultRepository = paymentResultRepository;
    }

    public List<PaymentResultDto> getAllPaymentResults() {
        return paymentResultRepository.findAll().stream()
                .map(TavridaMapper::toPaymentResultDto)
                .collect(Collectors.toList());
    }

    public PaymentResultDto getPaymentResult(Integer paymentResultId) {
        PaymentResult result = paymentResultRepository.findById(paymentResultId)
                .orElseThrow(() -> new EntityNotFoundException("Payment result not found: " + paymentResultId));
        return TavridaMapper.toPaymentResultDto(result);
    }

    @Transactional
    public PaymentResultDto createPaymentResult(PaymentResultDto dto) {
        PaymentResult result = TavridaMapper.toPaymentResultEntity(dto);
        PaymentResult saved = paymentResultRepository.save(result);
        return TavridaMapper.toPaymentResultDto(saved);
    }

    @Transactional
    public PaymentResultDto updatePaymentResult(Integer paymentResultId, PaymentResultDto dto) {
        PaymentResult existing = paymentResultRepository.findById(paymentResultId)
                .orElseThrow(() -> new EntityNotFoundException("Payment result not found: " + paymentResultId));
        existing.setPaymentResultName(dto.getPaymentResultName());
        PaymentResult updated = paymentResultRepository.save(existing);
        return TavridaMapper.toPaymentResultDto(updated);
    }

    @Transactional
    public void deletePaymentResult(Integer paymentResultId) {
        if (!paymentResultRepository.existsById(paymentResultId)) {
            throw new EntityNotFoundException("Payment result not found: " + paymentResultId);
        }
        paymentResultRepository.deleteById(paymentResultId);
    }
}