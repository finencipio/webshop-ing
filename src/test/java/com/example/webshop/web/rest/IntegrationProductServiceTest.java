package com.example.webshop.web.rest;

import com.example.webshop.model.common.Currency;
import com.example.webshop.model.dto.DTOProduct;
import com.example.webshop.model.repository.ProductRepository;
import com.example.webshop.service.HNBConverter;
import com.example.webshop.web.requests.CreateProductRequest;
import com.example.webshop.web.requests.UpdateProductRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class IntegrationProductServiceTest extends  AbstractServiceTest{
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private HNBConverter hnbConverter;

    @Test
    public void createProductUniqueCode_fail() throws Exception {
        String code = "1234567890";
        String testName = "a";
        double value = 10;
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
            .andExpect(status().isOk());

        mockMvc.perform(post("/product")
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(createProductRequest)))
            .andExpect(status().isBadRequest());
    }

    public void updateProduct(Currency currency, Currency updatedCurrency) throws Exception {
        String code = "1234567890";
        String testName = "a";
        double value = 10;
        String description = "description";
        boolean isAvailable = true;

        CreateProductRequest createProductRequest = CreateProductRequest.builder()
            .code(code)
            .name(testName)
            .value(value)
            .currency(currency)
            .description(description)
            .available(isAvailable)
            .build();

        String updatedCode = "2222222222";
        String updatedTestName = "updated";
        double updatedValue = 5;
        String updatedDescription = "udescription";
        boolean updatedIsAvailable = false;

        DTOProduct result = objectMapper.readValue(
            mockMvc.perform(post("/product")
                    .contentType("application/json")
                    .content(objectMapper.writeValueAsString(createProductRequest)))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString(),
            DTOProduct.class
        );

        UpdateProductRequest updateProductRequest = UpdateProductRequest.builder()
            .code(updatedCode)
            .name(updatedTestName)
            .value(updatedValue)
            .currency(updatedCurrency)
            .description(updatedDescription)
            .available(updatedIsAvailable)
            .build();

        DTOProduct updatedResult = objectMapper.readValue(
            mockMvc.perform(put("/product/{productId}", result.getId())
                    .contentType("application/json")
                    .content(objectMapper.writeValueAsString(updateProductRequest)))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString(),
            DTOProduct.class
        );

        assertEquals(updatedCode, updatedResult.getCode());
        assertEquals(updatedTestName, updatedResult.getName());
        assertEquals(updatedDescription, updatedResult.getDescription());
        assertEquals(hnbConverter.convert(updatedCurrency, Currency.HRK, updatedValue), updatedResult.getHrk());
        assertEquals(hnbConverter.convert(updatedCurrency, Currency.EUR, updatedValue), updatedResult.getEur());
        assertEquals(updatedIsAvailable, updatedResult.isAvailable());
    }

    @Test
    public void updateProductHrkToHrk_success() throws Exception {
        updateProduct(Currency.HRK, Currency.HRK);
    }

    @Test
    public void updateProductHrkToEur_success() throws Exception {
        updateProduct(Currency.HRK, Currency.EUR);
    }

    @Test
    public void updateProductEurToHrk_success() throws Exception {
        updateProduct(Currency.EUR, Currency.HRK);
    }

    @Test
    public void updateProductEurToEur_success() throws Exception {
        updateProduct(Currency.EUR, Currency.EUR);
    }

    @Test
    public void updateProduct_success() throws Exception {
        String code = "1234567890";
        String testName = "a";
        double value = 10;
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

        String updatedCode = "2222222222";
        String updatedTestName = "updated";
        double updatedValue = 5;
        Currency updatedHrk = Currency.HRK;
        String updatedDescription = "udescription";
        boolean updatedIsAvailable = false;

        DTOProduct result = objectMapper.readValue(
            mockMvc.perform(post("/product")
                    .contentType("application/json")
                    .content(objectMapper.writeValueAsString(createProductRequest)))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString(),
            DTOProduct.class
        );

        UpdateProductRequest updateProductRequest = UpdateProductRequest.builder()
            .code(updatedCode)
            .name(updatedTestName)
            .value(updatedValue)
            .currency(updatedHrk)
            .description(updatedDescription)
            .available(updatedIsAvailable)
            .build();

        DTOProduct updatedResult = objectMapper.readValue(
            mockMvc.perform(put("/product/{productId}", result.getId())
                    .contentType("application/json")
                    .content(objectMapper.writeValueAsString(updateProductRequest)))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString(),
            DTOProduct.class
        );

        assertEquals(updatedCode, updatedResult.getCode());
        assertEquals(updatedTestName, updatedResult.getName());
        assertEquals(updatedDescription, updatedResult.getDescription());
        assertEquals(updatedValue, updatedResult.getHrk());
        assertEquals(updatedValue / AbstractServiceTest.eurExchangeRate, updatedResult.getEur());
        assertEquals(updatedIsAvailable, updatedResult.isAvailable());
    }

    @Test
    public void updateProductShortCode_fail() throws Exception {
        String code = "1234567890";
        String testName = "a";
        double value = 10;
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

        String updatedCode = "222222222";
        String updatedTestName = "updated";
        double updatedValue = 5;
        Currency updatedHrk = Currency.HRK;
        String updatedDescription = "udescription";
        boolean updatedIsAvailable = false;

        DTOProduct result = objectMapper.readValue(
            mockMvc.perform(post("/product")
                    .contentType("application/json")
                    .content(objectMapper.writeValueAsString(createProductRequest)))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString(),
            DTOProduct.class
        );

        UpdateProductRequest updateProductRequest = UpdateProductRequest.builder()
            .code(updatedCode)
            .name(updatedTestName)
            .value(updatedValue)
            .currency(updatedHrk)
            .description(updatedDescription)
            .available(updatedIsAvailable)
            .build();

        mockMvc.perform(put("/product/{productId}", result.getId())
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(updateProductRequest)))
            .andExpect(status().isBadRequest());
    }

    @Test
    public void updateProductNegativeValue_fail() throws Exception {
        String code = "1234567890";
        String testName = "a";
        double value = 10;
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

        String updatedCode = "2222222222";
        String updatedTestName = "updated";
        double updatedValue = -1;
        Currency updatedHrk = Currency.HRK;
        String updatedDescription = "udescription";
        boolean updatedIsAvailable = false;

        DTOProduct result = objectMapper.readValue(
            mockMvc.perform(post("/product")
                    .contentType("application/json")
                    .content(objectMapper.writeValueAsString(createProductRequest)))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString(),
            DTOProduct.class
        );

        UpdateProductRequest updateProductRequest = UpdateProductRequest.builder()
            .code(updatedCode)
            .name(updatedTestName)
            .value(updatedValue)
            .currency(updatedHrk)
            .description(updatedDescription)
            .available(updatedIsAvailable)
            .build();

        mockMvc.perform(put("/product/{productId}", result.getId())
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(updateProductRequest)))
            .andExpect(status().isBadRequest());
    }
}
