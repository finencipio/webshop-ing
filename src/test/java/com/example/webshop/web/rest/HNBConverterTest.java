package com.example.webshop.web.rest;

import com.example.webshop.model.common.Currency;
import com.example.webshop.service.HNBConverter;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class HNBConverterTest extends AbstractServiceTest {

    @Autowired
    private HNBConverter hnbConverter;

    @Test
    public void hrkToHrk() {
        double hrk = 15;
        double converted = hnbConverter.convert(Currency.HRK, Currency.HRK, hrk);

        assertEquals(hrk, converted);
    }

    @Test
    public void hrkToEur() {
        double hrk = 15;
        double converted = hnbConverter.convert(Currency.HRK, Currency.EUR, hrk);

        assertEquals(hrk / AbstractServiceTest.eurExchangeRate, converted);
    }

    @Test
    public void eurToEur() {
        double eur = 15;
        double converted = hnbConverter.convert(Currency.EUR, Currency.EUR, eur);

        assertEquals(eur, converted);
    }

    @Test
    public void eurToHrk() {
        double eur = 15;
        double converted = hnbConverter.convert(Currency.EUR, Currency.HRK, eur);

        assertEquals(eur * AbstractServiceTest.eurExchangeRate, converted);
    }
}
