package ru.vtb.msa.detr.tavrida.api.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class PaymentDto {
    private Long paymentId;
    private LocalDateTime paymentTime;
    private Long cardId;
    private BigDecimal balanceBefore;
    private BigDecimal balanceAfter;
    private PaymentTypeDto paymentType;
    private PaymentResultDto paymentResult;
    private Long terminalId;

    public PaymentDto() {
    }

    // Геттеры и сеттеры
    public Long getPaymentId() {
        return paymentId;
    }

    public void setPaymentId(Long paymentId) {
        this.paymentId = paymentId;
    }

    public LocalDateTime getPaymentTime() {
        return paymentTime;
    }

    public void setPaymentTime(LocalDateTime paymentTime) {
        this.paymentTime = paymentTime;
    }

    public Long getCardId() {
        return cardId;
    }

    public void setCardId(Long cardId) {
        this.cardId = cardId;
    }

    public BigDecimal getBalanceBefore() {
        return balanceBefore;
    }

    public void setBalanceBefore(BigDecimal balanceBefore) {
        this.balanceBefore = balanceBefore;
    }

    public BigDecimal getBalanceAfter() {
        return balanceAfter;
    }

    public void setBalanceAfter(BigDecimal balanceAfter) {
        this.balanceAfter = balanceAfter;
    }

    public PaymentTypeDto getPaymentType() {
        return paymentType;
    }

    public void setPaymentType(PaymentTypeDto paymentType) {
        this.paymentType = paymentType;
    }

    public PaymentResultDto getPaymentResult() {
        return paymentResult;
    }

    public void setPaymentResult(PaymentResultDto paymentResult) {
        this.paymentResult = paymentResult;
    }

    public Long getTerminalId() {
        return terminalId;
    }

    public void setTerminalId(Long terminalId) {
        this.terminalId = terminalId;
    }
}