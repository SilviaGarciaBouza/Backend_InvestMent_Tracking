package com.silviagarcia.investtracking.Investment_Tracking.controller;


import com.silviagarcia.investtracking.Investment_Tracking.dto.TransactionDTO;
import com.silviagarcia.investtracking.Investment_Tracking.service.TransactionService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TransactionControllerTest {

    @Mock
    private TransactionService transactionService;

    @InjectMocks
    private TransactionController transactionController;

    @Test
    @DisplayName("Debería retornar 201 Created al registrar una nueva transacción")
    void create_DebeRetornarStatusCreatedYDatos() {
        // 1. Arrange (Preparar)
        Long itemId = 10L;
        TransactionDTO inputDto = new TransactionDTO();
        inputDto.setStocks(5.0);
        inputDto.setPurchasePrice(100.0);

        TransactionDTO savedDto = new TransactionDTO();
        savedDto.setId(1L);
        savedDto.setStocks(5.0);
        savedDto.setPurchasePrice(100.0);
        savedDto.setInvEur(500.0);
        savedDto.setItemId(itemId);
        savedDto.setPurchaseDate(LocalDateTime.now());

        // Configuramos el comportamiento del mock
        when(transactionService.createTransaction(any(TransactionDTO.class), eq(itemId)))
                .thenReturn(savedDto);

        // 2. Act (Actuar)
        ResponseEntity<TransactionDTO> response = transactionController.create(inputDto, itemId);

        // 3. Assert (Verificar)
        assertNotNull(response);
        assertEquals(HttpStatus.CREATED, response.getStatusCode(), "El status debe ser 201");
        assertNotNull(response.getBody());
        assertEquals(1L, response.getBody().getId());
        assertEquals(500.0, response.getBody().getInvEur());

        // Verificamos que el controlador realmente llamó al servicio
        verify(transactionService, times(1)).createTransaction(any(TransactionDTO.class), eq(itemId));
    }
}
