package com.achegueng.logitrack.models;

import com.achegueng.logitrack.enums.payment_method;
import jakarta.json.bind.annotation.JsonbTransient;
import jakarta.persistence.*;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "Payment")
public class Payment implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private Long paymentId;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "orderId")
    @JsonbTransient
    private Order orderId;

    @Column
    private LocalDate paymentDate;

    @Column
    private BigDecimal amount;

    @Enumerated(EnumType.STRING)
    private payment_method method;

    @PrePersist
    public void prePersist() {
        if (paymentDate == null) {
            paymentDate = LocalDate.now();
        }
    }

    //Getters and Setters


    public Long getPaymentId() {
        return paymentId;
    }

    public void setPaymentId(Long paymentId) {
        this.paymentId = paymentId;
    }

    public Order getOrderId() {
        return orderId;
    }

    public void setOrderId(Order orderId) {
        this.orderId = orderId;
    }

    public LocalDate getPaymentDate() {
        return paymentDate;
    }

    public void setPaymentDate(LocalDate paymentDate) {
        this.paymentDate = paymentDate;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public payment_method getMethod() {
        return method;
    }

    public void setMethod(payment_method method) {
        this.method = method;
    }

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("Category{");
        sb.append("paymentId=").append(paymentId);
        sb.append(", orderId='").append(orderId).append('\'');
        sb.append(", paymentDate='").append(paymentDate).append('\'');
        sb.append(", amount='").append(amount).append('\'');
        sb.append(", method='").append(method);
        sb.append('}');
        return sb.toString();
    }

}
