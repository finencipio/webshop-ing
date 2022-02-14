package com.example.webshop.web.requests;

import com.example.webshop.model.common.Currency;
import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Data
@Builder
public class UpdateProductRequest implements Serializable {
    @NotNull
    private String code;
    @NotNull
    private String name;
    private double value;
    @NotNull
    private Currency currency;
    @NotNull
    private String description;
    private boolean available;
}
