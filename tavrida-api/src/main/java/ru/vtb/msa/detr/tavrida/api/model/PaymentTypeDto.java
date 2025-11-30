package ru.vtb.msa.detr.tavrida.api.model;

public class PaymentTypeDto {
    private Integer paymentTypeId;
    private String paymentTypeName;

    public PaymentTypeDto() {}

    public Integer getPaymentTypeId() { return paymentTypeId; }
    public void setPaymentTypeId(Integer paymentTypeId) { this.paymentTypeId = paymentTypeId; }

    public String getPaymentTypeName() { return paymentTypeName; }
    public void setPaymentTypeName(String paymentTypeName) { this.paymentTypeName = paymentTypeName; }
}