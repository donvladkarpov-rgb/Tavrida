package ru.vtb.msa.detr.tavrida.core.model;

import jakarta.persistence.*;

@Entity
@Table(name = "payment_results")
public class PaymentResult {

    @Id
    @Column(name = "payment_result_id")
    private Integer paymentResultId;

    @Column(name = "payment_result_name", length = 255, nullable = false)
    private String paymentResultName;

    // Constructors
    public PaymentResult() {}

    public PaymentResult(Integer paymentResultId, String paymentResultName) {
        this.paymentResultId = paymentResultId;
        this.paymentResultName = paymentResultName;
    }

    // Getters and Setters
    public Integer getPaymentResultId() { return paymentResultId; }
    public void setPaymentResultId(Integer paymentResultId) { this.paymentResultId = paymentResultId; }

    public String getPaymentResultName() { return paymentResultName; }
    public void setPaymentResultName(String paymentResultName) { this.paymentResultName = paymentResultName; }
}