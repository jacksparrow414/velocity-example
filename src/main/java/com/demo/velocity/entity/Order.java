package com.demo.velocity.entity;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Collection;

@Data
public class Order {

    private String customerName;

    private Collection<String> items;

    private BigDecimal paymentAmount;

    private LocalDateTime paymentTime;

    private String deliveryMethod;
}
