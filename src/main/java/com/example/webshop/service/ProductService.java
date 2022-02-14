package com.example.webshop.service;

import com.example.webshop.model.common.Currency;
import com.example.webshop.model.dto.DTOProduct;
import com.example.webshop.model.entity.Product;
import com.example.webshop.model.repository.ProductRepository;
import com.example.webshop.web.requests.CreateProductRequest;
import com.example.webshop.web.requests.UpdateProductRequest;

import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.List;
import java.util.Objects;

import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;

@Component
@Validated
@RequiredArgsConstructor
public class ProductService {
    private final ProductRepository productRepository;
    private final HNBConverter hnbConverter;
    private final ModelMapper modelMapper;

    public DTOProduct createProduct(@Valid CreateProductRequest createProductRequest) {
        Product entity = Product.builder()
            .code(createProductRequest.getCode())
            .description(createProductRequest.getDescription())
            .priceEur(hnbConverter.convert(createProductRequest.getCurrency(), Currency.EUR, createProductRequest.getValue()))
            .priceHrk(hnbConverter.convert(createProductRequest.getCurrency(), Currency.HRK, createProductRequest.getValue()))
            .name(createProductRequest.getName())
            .available(createProductRequest.isAvailable())
            .build();

        productRepository.save(entity);
        return modelMapper.map(entity, DTOProduct.class);
    }

    public DTOProduct updateProduct(Long productId, @Valid UpdateProductRequest updateProductRequest) {
        Product entity = productRepository.getById(productId);

        if(Objects.isNull(entity)) {
            throw new IllegalArgumentException("Id not exists");
        }

        entity.setAvailable(updateProductRequest.isAvailable());
        entity.setCode(updateProductRequest.getCode());
        entity.setDescription(updateProductRequest.getDescription());
        entity.setPriceHrk(
            hnbConverter.convert(updateProductRequest.getCurrency(), Currency.HRK, updateProductRequest.getValue())
        );
        entity.setPriceEur(
            hnbConverter.convert(updateProductRequest.getCurrency(), Currency.EUR, updateProductRequest.getValue())
        );
        entity.setName(updateProductRequest.getName());

        productRepository.save(entity);
        return modelMapper.map(entity, DTOProduct.class);
    }

    public Collection<DTOProduct> getAllProducts() {
        List<Product> entityList = productRepository.findAll();
        return modelMapper.map(entityList, new TypeToken<List<DTOProduct>>(){}.getType());
    }

    public Collection<DTOProduct> getProductsByAvailability(boolean available) {
        Collection<Product> entityList = productRepository.findProductsByAvailable(available);
        return modelMapper.map(entityList, new TypeToken<List<DTOProduct>>(){}.getType());
    }

    public void deleteByCode(String code) {
        productRepository.deleteByCode(code);
    }

    public void deleteById(Long id) {
        productRepository.deleteById(id);
    }

    public DTOProduct getById(Long id) {
        return modelMapper.map(productRepository.getById(id), DTOProduct.class);
    }
}
