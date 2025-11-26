package com.achegueng.logitrack.models;

import com.achegueng.logitrack.enums.order_status;
import jakarta.json.bind.annotation.JsonbTransient;
import jakarta.persistence.*;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "Orders")
public class Order implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private Long orderId;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "customerId")
    @JsonbTransient
    private Customer customer;

    @Column
    private LocalDate orderDate;

    @Enumerated(EnumType.STRING)
    private order_status status;

    @Column
    private BigDecimal totalAmount;

    @OneToMany(mappedBy = "orderId", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<OrderItem> items;

    @PrePersist
    public void prePersist() {
        if (status == null) {
            status = order_status.Pending; // tu enum
        }
        if (orderDate == null) {
            orderDate = LocalDate.now();
        }
    }


    //Getters and Setters

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public LocalDate getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(LocalDate orderDate) {
        this.orderDate = orderDate;
    }

    public order_status getStatus() {
        return status;
    }

    public void setStatus(order_status status) {
        this.status = status;
    }

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }
    public List<OrderItem> getItems() {
        return items;
    }

    public void setItems(List<OrderItem> items) {
        this.items = items;
    }

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("Category{");
        sb.append("orderId=").append(orderId);
        sb.append(", customer='").append(customer).append('\'');
        sb.append(", orderDate='").append(orderDate).append('\'');
        sb.append(", status='").append(status).append('\'');
        sb.append(", totalAmount='").append(totalAmount).append('\'');
        sb.append(", items='").append(items);
        sb.append('}');
        return sb.toString();
    }
}
