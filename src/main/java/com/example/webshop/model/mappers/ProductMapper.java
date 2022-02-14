package com.example.webshop.model.mappers;

import com.example.webshop.model.dto.DTOProduct;
import com.example.webshop.model.entity.Product;

import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ProductMapper
{

    @Bean
    public ModelMapper modelMapper()
    {
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.typeMap(Product.class, DTOProduct.class)
            .addMappings(mapper -> {
                mapper.map(Product::getPriceHrk, DTOProduct::setHrk);
                mapper.map(Product::getPriceEur, DTOProduct::setEur);
            });
        return modelMapper;
    }


}
