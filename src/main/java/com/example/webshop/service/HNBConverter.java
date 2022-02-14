package com.example.webshop.service;

import com.example.webshop.model.common.Currency;
import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import java.text.NumberFormat;
import java.text.ParseException;
import java.util.*;
import java.util.function.BiFunction;

@Component
public class HNBConverter {
    private static final Map<Currency, Map<Currency, BiFunction<Double, ExchangeRates, Double>>> converters;
    static {
        converters = new HashMap<>();

        HashMap<Currency, BiFunction<Double, ExchangeRates, Double>> eurConverters = new HashMap<>();
        converters.put(Currency.EUR, eurConverters);

        eurConverters.put(Currency.HRK, (value, exchangeRates) -> value * exchangeRates.eur);

        HashMap<Currency, BiFunction<Double, ExchangeRates, Double>> hrkConverters = new HashMap<>();
        converters.put(Currency.HRK, hrkConverters);

        hrkConverters.put(Currency.EUR, (value, exchangeRates) -> value / exchangeRates.eur);
    }
    private static final String EXCHANGE_RATE_KEYWORD = "Srednji za devize";
    public static final String VALUTE_PARAM_NAME = "valuta";
    public static String BASE_URL;
    private static final NumberFormat API_NUMBER_FORMAT = NumberFormat.getInstance(new Locale("hr"));

    private WebClient webClient;

    private ExchangeRates exchangeRates;


    public HNBConverter(@Value("${hnb.base-url}") String baseUrl) {
        BASE_URL = baseUrl;
        webClient = WebClient.builder()
            .baseUrl(BASE_URL)
            .build();
    }

    private void update() {
        exchangeRates = new ExchangeRates();

        Map<String, String>[] responseBody = Objects.requireNonNull(webClient.get()
            .uri(uriBuilder -> uriBuilder
                .queryParam(VALUTE_PARAM_NAME, Currency.EUR)
                .build())
            .retrieve().bodyToMono(new ParameterizedTypeReference<Map<String, String>[]>() {
            }).block());

        Map<String, String> eurMap = responseBody[0];

        Number number = null;
        try {
            number = API_NUMBER_FORMAT.parse(eurMap.get(EXCHANGE_RATE_KEYWORD));
        } catch (ParseException e) {
            throw new IllegalStateException("Valute converting not available");
        }

        exchangeRates.eur = number.doubleValue();
    }

    public double getEuroExchangeRate() {
        update();
        return exchangeRates.eur;
    }

    public double convert(Currency in, Currency out, double value) {
        if(in.equals(out)) {
            return value;
        }
        if(exchangeRates == null) {
            update();
        }
        try {
            return converters.get(in).get(out).apply(value, exchangeRates);
        } catch (NullPointerException ex) {
            throw new IllegalArgumentException(String.format("Mapping from %s to %s is not available", in, out));
        }
    }

    private static class ExchangeRates {
        double eur;
    }

    @Data
    private static class HNBApiResponse {
        private Map<String, String>[] valuteInformations;
    }
}
