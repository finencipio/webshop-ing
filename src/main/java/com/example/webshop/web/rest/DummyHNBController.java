package com.example.webshop.web.rest;

import com.example.webshop.service.HNBConverter;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DummyHNBController {
    private HNBConverter hnbConverter;

    @Autowired
    public DummyHNBController(HNBConverter hnbConverter) {
        this.hnbConverter = hnbConverter;
    }

    @GetMapping("/eur")
    private double getEuroExchangeRate() {
        return hnbConverter.getEuroExchangeRate();
    }
}
