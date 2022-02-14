package com.example.webshop.web.rest;

import com.example.webshop.model.dto.DTOProduct;
import com.example.webshop.service.ProductService;
import com.example.webshop.web.requests.CreateProductRequest;
import com.example.webshop.web.requests.UpdateProductRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.Collection;

@RestController
public class ProductController {
    private ProductService productService;

    @Autowired
    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @PostMapping("/product")
    public DTOProduct createProduct(@Valid @RequestBody CreateProductRequest createProductRequest) {
        return productService.createProduct(createProductRequest);
    }

    @PutMapping("/product/{productId}")
    public DTOProduct updateProduct(@PathVariable Long productId, @RequestBody UpdateProductRequest updateProductRequest) {
        return productService.updateProduct(productId, updateProductRequest);
    }

    @GetMapping("/product/{productId}")
    public DTOProduct getProduct(@PathVariable Long productId) {
        return productService.getById(productId);
    }

    @GetMapping("/product")
    public Collection<DTOProduct> getProducts() {
        return productService.getAllProducts();
    }

    @GetMapping("/product/available/{available}")
    public Collection<DTOProduct> getProductsByAvailability(@PathVariable boolean available) {
        return productService.getProductsByAvailability(available);
    }

    @DeleteMapping("/product/{productId}")
    public void deleteProductById(@PathVariable Long productId) {
        productService.deleteById(productId);
    }

    @DeleteMapping("/product/code/{productCode}")
    public void deleteProductById(@PathVariable String productCode) {
        productService.deleteByCode(productCode);
    }

    @ResponseStatus(value= HttpStatus.BAD_REQUEST)
    @ExceptionHandler(Exception.class)
    public ModelAndView handleError(HttpServletRequest req, Exception ex) {
        ModelAndView mav = new ModelAndView();
        mav.addObject("exception", ex);
        mav.addObject("url", req.getRequestURL());
        mav.addObject("message", ex.getMessage());
        mav.setViewName("error");
        return mav;
    }

    @ResponseStatus(value= HttpStatus.BAD_REQUEST)
    @ExceptionHandler(DataIntegrityViolationException.class)
    public ModelAndView handleConstraintViolationError(HttpServletRequest req, DataIntegrityViolationException ex) {
        ModelAndView mav = new ModelAndView();
        mav.addObject("exception", ex);
        mav.addObject("url", req.getRequestURL());
        mav.addObject("message", ex.getCause().getCause().getMessage());
        mav.setViewName("error");
        return mav;
    }
}
