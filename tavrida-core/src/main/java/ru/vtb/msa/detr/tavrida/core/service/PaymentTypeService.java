package ru.vtb.msa.detr.tavrida.core.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.vtb.msa.detr.tavrida.api.model.PaymentTypeDto;
import ru.vtb.msa.detr.tavrida.core.exception.EntityNotFoundException;
import ru.vtb.msa.detr.tavrida.core.model.PaymentType;
import ru.vtb.msa.detr.tavrida.core.model.mapper.TavridaMapper;
import ru.vtb.msa.detr.tavrida.core.repo.PaymentTypeRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class PaymentTypeService {

    private final PaymentTypeRepository paymentTypeRepository;

    public PaymentTypeService(PaymentTypeRepository paymentTypeRepository) {
        this.paymentTypeRepository = paymentTypeRepository;
    }

    public List<PaymentTypeDto> getAllPaymentTypes() {
        return paymentTypeRepository.findAll().stream()
                .map(TavridaMapper::toPaymentTypeDto)
                .collect(Collectors.toList());
    }

    public PaymentTypeDto getPaymentType(Integer paymentTypeId) {
        PaymentType type = paymentTypeRepository.findById(paymentTypeId)
                .orElseThrow(() -> new EntityNotFoundException("Payment type not found: " + paymentTypeId));
        return TavridaMapper.toPaymentTypeDto(type);
    }

    @Transactional
    public PaymentTypeDto createPaymentType(PaymentTypeDto dto) {
        PaymentType type = TavridaMapper.toPaymentTypeEntity(dto);
        PaymentType saved = paymentTypeRepository.save(type);
        return TavridaMapper.toPaymentTypeDto(saved);
    }

    @Transactional
    public PaymentTypeDto updatePaymentType(Integer paymentTypeId, PaymentTypeDto dto) {
        PaymentType existing = paymentTypeRepository.findById(paymentTypeId)
                .orElseThrow(() -> new EntityNotFoundException("Payment type not found: " + paymentTypeId));
        existing.setPaymentTypeName(dto.getPaymentTypeName());
        PaymentType updated = paymentTypeRepository.save(existing);
        return TavridaMapper.toPaymentTypeDto(updated);
    }

    @Transactional
    public void deletePaymentType(Integer paymentTypeId) {
        if (!paymentTypeRepository.existsById(paymentTypeId)) {
            throw new EntityNotFoundException("Payment type not found: " + paymentTypeId);
        }
        paymentTypeRepository.deleteById(paymentTypeId);
    }
}