package com.silviagarcia.investtracking.Investment_Tracking.service;


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
        // Arrange
        String symbol = "BTCUSDT";
        Map<String, String> binanceResponse = new HashMap<>();
        binanceResponse.put("price", "60000.00");

        when(restTemplate.getForObject(contains("binance.com"), eq(Map.class)))
                .thenReturn(binanceResponse);

        // Act
        Double price = priceService.getRealTimePrice(symbol);

        // Assert
        assertEquals(60000.0, price);
    }
}
