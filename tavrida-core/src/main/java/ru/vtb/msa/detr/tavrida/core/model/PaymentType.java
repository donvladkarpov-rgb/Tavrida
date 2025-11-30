package ru.vtb.msa.detr.tavrida.core.model;

import jakarta.persistence.*;

@Entity
@Table(name = "payment_types")
public class PaymentType {

    @Id
    @Column(name = "payment_type_id")
    private Integer paymentTypeId;

    @Column(name = "payment_type_name", length = 255, nullable = false)
    private String paymentTypeName;

    // Constructors
    public PaymentType() {}

    public PaymentType(Integer paymentTypeId, String paymentTypeName) {
        this.paymentTypeId = paymentTypeId;
        this.paymentTypeName = paymentTypeName;
    }

    // Getters and Setters
    public Integer getPaymentTypeId() { return paymentTypeId; }
    public void setPaymentTypeId(Integer paymentTypeId) { this.paymentTypeId = paymentTypeId; }

    public String getPaymentTypeName() { return paymentTypeName; }
    public void setPaymentTypeName(String paymentTypeName) { this.paymentTypeName = paymentTypeName; }
}