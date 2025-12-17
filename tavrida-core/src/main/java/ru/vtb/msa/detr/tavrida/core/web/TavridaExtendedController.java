package ru.vtb.msa.detr.tavrida.core.web;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import ru.vtb.msa.detr.tavrida.api.model.*;
import ru.vtb.msa.detr.tavrida.api.web.TavridaApiExtended;
import ru.vtb.msa.detr.tavrida.core.service.*;

import java.util.List;
import java.util.UUID;

@RestController
public class TavridaExtendedController implements TavridaApiExtended {

    private final SessionService sessionService;
    private final CodeService codeService;
    private final MasterPasswordService masterPasswordService;
    private final ServiceEventService serviceEventService;
    private final PaymentService paymentService;
    private final ServiceEventTypeService eventTypeService;
    private final PaymentTypeService paymentTypeService;
    private final PaymentResultService paymentResultService;

    public TavridaExtendedController(
            SessionService sessionService,
            CodeService codeService,
            MasterPasswordService masterPasswordService,
            ServiceEventService serviceEventService,
            PaymentService paymentService,
            ServiceEventTypeService eventTypeService,
            PaymentTypeService paymentTypeService,
            PaymentResultService paymentResultService) {
        this.sessionService = sessionService;
        this.codeService = codeService;
        this.masterPasswordService = masterPasswordService;
        this.serviceEventService = serviceEventService;
        this.paymentService = paymentService;
        this.eventTypeService = eventTypeService;
        this.paymentTypeService = paymentTypeService;
        this.paymentResultService = paymentResultService;
    }

    // ========== SESSIONS ==========
    @Override
    public ResponseEntity<UserSessionDto> createSession(UserSessionDto dto) {
        return ResponseEntity.ok(sessionService.createSession(dto));
    }

    @Override
    public ResponseEntity<UserSessionDto> getSession(UUID sessionId) {
        return ResponseEntity.ok(sessionService.getSession(sessionId));
    }

    @Override
    public ResponseEntity<Boolean> isSessionValid(UUID sessionId) {
        return ResponseEntity.ok(sessionService.isValid(sessionId));
    }

    // ========== CODES ==========
    @Override
    public ResponseEntity<CodeDto> createCode(CodeDto dto) {
        return ResponseEntity.ok(codeService.createCode(dto));
    }

    @Override
    public ResponseEntity<CodeDto> getCode(String codeUid) {
        return ResponseEntity.ok(codeService.getCode(codeUid));
    }

    @Override
    public ResponseEntity<CodeDto> updateCode(String codeUid, CodeDto dto) {
        return ResponseEntity.ok(codeService.updateCode(codeUid, dto));
    }

    @Override
    public ResponseEntity<Void> deleteCode(String codeUid) {
        codeService.deleteCode(codeUid);
        return ResponseEntity.noContent().build();
    }

    // ========== MASTER PASSWORD ==========
    @Override
    public ResponseEntity<MasterPasswordDto> setMasterPassword(MasterPasswordDto dto) {
        return ResponseEntity.ok(masterPasswordService.setMasterPassword(dto));
    }

    @Override
    public ResponseEntity<MasterPasswordDto> getMasterPassword() {
        MasterPasswordDto mp = masterPasswordService.getMasterPassword();
        return mp != null ? ResponseEntity.ok(mp) : ResponseEntity.notFound().build();
    }

    // ========== AUDIT ==========
    @Override
    public ResponseEntity<ServiceEventDto> logEvent(ServiceEventDto dto) {
        return ResponseEntity.ok(serviceEventService.logEvent(dto));
    }

    @Override
    public ResponseEntity<List<ServiceEventDto>> getEventsByUser(Long userId) {
        return ResponseEntity.ok(serviceEventService.getEventsByUser(userId));
    }

    @Override
    public ResponseEntity<List<ServiceEventDto>> getEventsByReference(Long referenceId) {
        return ResponseEntity.ok(serviceEventService.getEventsByReference(referenceId));
    }

    // ========== PAYMENTS ==========
    @Override
    public ResponseEntity<PaymentDto> createPayment(PaymentDto dto) {
        return ResponseEntity.ok(paymentService.createPayment(dto));
    }

    @Override
    public ResponseEntity<List<PaymentDto>> getPaymentsByCard(Long cardId) {
        return ResponseEntity.ok(paymentService.getPaymentsByCard(cardId));
    }

    @Override
    public ResponseEntity<List<PaymentDto>> getPaymentsByTerminal(Long terminalId) {
        return ResponseEntity.ok(paymentService.getPaymentsByTerminal(terminalId));
    }

    // ========== REFERENCE DATA ==========
    @Override
    public ResponseEntity<List<ServiceEventTypeDto>> getAllEventTypes() {
        return ResponseEntity.ok(eventTypeService.getAllEventTypes());
    }

    @Override
    public ResponseEntity<List<PaymentTypeDto>> getAllPaymentTypes() {
        return ResponseEntity.ok(paymentTypeService.getAllPaymentTypes());
    }

    @Override
    public ResponseEntity<List<PaymentResultDto>> getAllPaymentResults() {
        return ResponseEntity.ok(paymentResultService.getAllPaymentResults());
    }
}