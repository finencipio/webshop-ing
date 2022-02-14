package com.example.webshop.web.rest;

import com.github.tomakehurst.wiremock.WireMockServer;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.text.NumberFormat;
import java.util.Locale;

import static com.github.tomakehurst.wiremock.client.WireMock.*;

@SpringBootTest
@ActiveProfiles("test")
abstract class AbstractServiceTest
{
    private static WireMockServer wireMockServer;

    public static double eurExchangeRate = 7.000528000;

    @BeforeAll
    public static void beforeAll() {
        wireMockServer = new WireMockServer(8080);
        wireMockServer.start();
        NumberFormat nf = NumberFormat.getInstance(new Locale("hr"));
        nf.setMaximumFractionDigits(Integer.MAX_VALUE);
        stubFor(get(urlEqualTo("/tecajn/v1?valuta=EUR")).willReturn(aResponse()
            .withHeader("Content-Type", "application/json;charset=UTF-8")
            .withBody(
            "[\n" +
                "    {\n" +
                "        \"Broj tečajnice\": \"16\",\n" +
                "        \"Datum primjene\": \"25.01.2022\",\n" +
                "        \"Država\": \"EMU\",\n" +
                "        \"Šifra valute\": \"978\",\n" +
                "        \"Valuta\": \"EUR\",\n" +
                "        \"Jedinica\": 1,\n" +
                "        \"Kupovni za devize\": \"7,505416\",\n" +
                "        \"Srednji za devize\": \"" + nf.format(eurExchangeRate) +
                "\",\n" +
                "        \"Prodajni za devize\": \"7,550584\"\n" +
                "    }\n" +
                "]\n")));
    }

    @AfterAll
    public static void afterAll() {
        wireMockServer.stop();
    }
}
