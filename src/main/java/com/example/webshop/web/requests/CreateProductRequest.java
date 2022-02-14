package com.example.webshop.web.requests;

import com.example.webshop.model.common.Currency;

import lombok.Builder;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Data
@Builder
public class CreateProductRequest implements Serializable {
    @Length(min=10, max=10)
    @NotNull
    private String code;
    @NotNull
    private String name;
    @Min(value = 0, message = "Value must be larger or equal to 0")
    private double value;
    @NotNull
    private Currency currency;
    @NotNull
    private String description;
    private boolean available;
}
