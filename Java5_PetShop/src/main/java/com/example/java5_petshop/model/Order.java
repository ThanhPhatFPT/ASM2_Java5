package com.example.java5_petshop.model;

import jakarta.persistence.*;
import lombok.Data;

import java.util.Date;

@Data
@Entity
@Table(name = "orders")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long orderId;
    private Date oderDate;
    private Double totalAmount;
    private boolean status;

    @ManyToOne
    @JoinColumn(name = "userId")
    private User user;
}
