package com.example.webshop.web.rest;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import com.example.webshop.model.common.Currency;
import com.example.webshop.model.entity.Product;
import com.example.webshop.model.repository.ProductRepository;
import com.example.webshop.service.HNBConverter;
import com.example.webshop.service.ProductService;
import com.example.webshop.web.requests.CreateProductRequest;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.validation.beanvalidation.MethodValidationPostProcessor;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@AutoConfigureMockMvc
public class ProductServiceTest extends AbstractServiceTest {
    @Autowired
    private HNBConverter hnbConverter;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private ModelMapper modelMapper;

    @Mock
    private ProductRepository productRepository;

    @Test
    public void createProductWithHrk_success() {
        ProductService productService = new ProductService(productRepository, hnbConverter, modelMapper);
        String code = "code567891";
        String testName = "testName";
        double value = 43;
        Currency eur = Currency.HRK;
        String description = "description";
        boolean isAvailable = true;

        CreateProductRequest createProductRequest = CreateProductRequest.builder()
            .code(code)
            .name(testName)
            .value(value)
            .currency(eur)
            .description(description)
            .available(isAvailable)
            .build();
        productService.createProduct(createProductRequest);

        ArgumentCaptor<Product> captor = ArgumentCaptor.forClass(Product.class);
        verify(productRepository).save(captor.capture());

        Product value1 = captor.getValue();

        assertEquals(code, value1.getCode());
        assertEquals(testName, value1.getName());
        assertTrue(Math.abs(value1.getPriceEur() - value / AbstractServiceTest.eurExchangeRate) < 1e-3);
        assertTrue(Math.abs(value1.getPriceHrk() - value) < 1e-3);
        assertEquals(description, value1.getDescription());
        assertEquals(isAvailable, value1.isAvailable());
    }

    @Test
    public void createProductWithEur_success() {
        ProductService productService = new ProductService(productRepository, hnbConverter, modelMapper);
        String code = "code567891";
        String testName = "testName";
        double value = 43;
        Currency eur = Currency.EUR;
        String description = "description";
        boolean isAvailable = true;

        CreateProductRequest createProductRequest = CreateProductRequest.builder()
            .code(code)
            .name(testName)
            .value(value)
            .currency(eur)
            .description(description)
            .available(isAvailable)
            .build();
        productService.createProduct(createProductRequest);

        ArgumentCaptor<Product> captor = ArgumentCaptor.forClass(Product.class);
        verify(productRepository).save(captor.capture());

        Product value1 = captor.getValue();

        assertEquals(code, value1.getCode());
        assertEquals(testName, value1.getName());
        assertTrue(Math.abs(value1.getPriceHrk() - value * AbstractServiceTest.eurExchangeRate) < 1e-3);
        assertTrue(Math.abs(value1.getPriceEur() - value) < 1e-3);
        assertEquals(description, value1.getDescription());
        assertEquals(isAvailable, value1.isAvailable());
    }


    @TestConfiguration
    static class TestContextConfiguration {
        @Bean
        public MethodValidationPostProcessor bean() {
            return new MethodValidationPostProcessor();
        }
    }

    @Test
    public void createProductShortCode_fail() throws Exception {
        String code = "123456789";
        String testName = "a";
        double value = 43;
        Currency eur = Currency.EUR;
        String description = "description";
        boolean isAvailable = true;

        CreateProductRequest createProductRequest = CreateProductRequest.builder()
            .code(code)
            .name(testName)
            .value(value)
            .currency(eur)
            .description(description)
            .available(isAvailable)
            .build();

        mockMvc.perform(post("/product")
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(createProductRequest)))
            .andExpect(status().isBadRequest());
    }

    @Test
    public void createProductNegativeValue_fail() throws Exception {
        String code = "1234567890";
        String testName = "a";
        double value = -1;
        Currency eur = Currency.EUR;
        String description = "description";
        boolean isAvailable = true;

        CreateProductRequest createProductRequest = CreateProductRequest.builder()
            .code(code)
            .name(testName)
            .value(value)
            .currency(eur)
            .description(description)
            .available(isAvailable)
            .build();

        mockMvc.perform(post("/product")
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(createProductRequest)))
            .andExpect(status().isBadRequest());
    }
}
