package com.silviagarcia.investtracking.investment_tracking.service;


import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PriceServiceTest {

    @Mock private RestTemplate restTemplate;
    @InjectMocks private PriceService priceService;

    @Test
    void testGetCryptoPrice_ShouldReturnPriceFromBinance() {
        String symbol = "BTCUSDT";
        Map<String, String> binanceResponse = new HashMap<>();
        binanceResponse.put("price", "60000.00");

        when(restTemplate.getForObject(contains("binance.com"), eq(Map.class)))
                .thenReturn(binanceResponse);

        Double price = priceService.getRealTimePrice(symbol);

        assertEquals(60000.0, price);
    }

    @Test
    void testGetStockPrice_ShouldReturnPriceFromFinnhub() {
        String symbol = "AAPL";
        Map<String, Object> finnhubResponse = new HashMap<>();
        finnhubResponse.put("c", "180.5");

        when(restTemplate.getForObject(contains("finnhub.io"), eq(Map.class)))
                .thenReturn(finnhubResponse);

        Double price = priceService.getRealTimePrice(symbol);

        assertEquals(180.5, price);
    }

    @Test
    void testGetForexPrice_ShouldReturnPriceFromErApi() {
        String symbol = "USD/EUR";
        Map<String, Object> rates = new HashMap<>();
        rates.put("EUR", "0.92");
        Map<String, Object> erApiResponse = new HashMap<>();
        erApiResponse.put("rates", rates);

        when(restTemplate.getForObject(contains("er-api.com"), eq(Map.class)))
                .thenReturn(erApiResponse);

        Double price = priceService.getRealTimePrice(symbol);

        assertEquals(0.92, price);
    }

    @Test
    void testGetStockPrice_ShouldReturnZero_OnApiError() {
        when(restTemplate.getForObject(contains("finnhub.io"), eq(Map.class)))
                .thenThrow(new RuntimeException("timeout"));

        Double price = priceService.getRealTimePrice("MSFT");

        assertEquals(0.0, price);
    }

    @Test
    void testGetBatchPrices_ShouldReturnEmpty_WhenNullOrEmpty() {
        assertTrue(priceService.getBatchPrices(null).isEmpty());
        assertTrue(priceService.getBatchPrices("").isEmpty());
    }

    @Test
    void testGetBatchPrices_ShouldFetchAllSymbols() {
        Map<String, String> btcResponse = new HashMap<>();
        btcResponse.put("price", "60000.00");
        Map<String, String> ethResponse = new HashMap<>();
        ethResponse.put("price", "3000.00");

        when(restTemplate.getForObject(contains("BTCUSDT"), eq(Map.class))).thenReturn(btcResponse);
        when(restTemplate.getForObject(contains("ETHUSDT"), eq(Map.class))).thenReturn(ethResponse);

        Map<String, Object> result = priceService.getBatchPrices("BTCUSDT,ETHUSDT");

        assertTrue(result.containsKey("BTCUSDT"));
        assertTrue(result.containsKey("ETHUSDT"));
    }
}
