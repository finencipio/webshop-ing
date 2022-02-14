package com.example.webshop.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class DTOProduct implements Serializable {
    private Long id;

    private String code;

    private String name;

    private double hrk;

    private double eur;

    private String description;

    private boolean available;
}
