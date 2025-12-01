package ru.vtb.msa.detr.tavrida.api.model;

public class PaymentResultDto {
    private Integer paymentResultId;
    private String paymentResultName;

    public PaymentResultDto() {}

    public PaymentResultDto(Integer paymentResultId) {
        this.paymentResultId = paymentResultId;
    }

    public Integer getPaymentResultId() { return paymentResultId; }
    public void setPaymentResultId(Integer paymentResultId) { this.paymentResultId = paymentResultId; }

    public String getPaymentResultName() { return paymentResultName; }
    public void setPaymentResultName(String paymentResultName) { this.paymentResultName = paymentResultName; }
}