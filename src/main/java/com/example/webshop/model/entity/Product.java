package com.example.webshop.model.entity;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Entity
@Table(name = "product")
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class Product {
    @Id
    @GeneratedValue
    private Long id;

    @Column(unique = true, length = 10)
    @Size(min=10, max=10)
    @NotNull
    private String code;

    @NotNull
    private String name;

    @Column(name = "price_hrk")
    @Min(value = 0)
    double priceHrk;

    @Column(name = "price_eur")
    @Min(value = 0)
    private double priceEur;

    @NotNull
    private String description;

    private boolean available;
}
