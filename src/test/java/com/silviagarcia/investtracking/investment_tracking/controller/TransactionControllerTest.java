package com.silviagarcia.investtracking.investment_tracking.controller;

import com.silviagarcia.investtracking.investment_tracking.dto.TransactionDTO;
import com.silviagarcia.investtracking.investment_tracking.service.TransactionService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TransactionControllerTest {

    @Mock private TransactionService transactionService;
    @InjectMocks private TransactionController transactionController;

    @Test
    @DisplayName("Debería retornar 201 Created al registrar una nueva transacción")
    void create_DebeRetornarStatusCreatedYDatos() {
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

        Authentication auth = mock(Authentication.class);
        when(auth.getName()).thenReturn("test@example.com");
        when(transactionService.createTransaction(any(TransactionDTO.class), eq(itemId), eq("test@example.com")))
                .thenReturn(savedDto);

        ResponseEntity<TransactionDTO> response = transactionController.create(inputDto, itemId, auth);

        assertNotNull(response);
        assertEquals(HttpStatus.CREATED, response.getStatusCode(), "El status debe ser 201");
        assertNotNull(response.getBody());
        assertEquals(1L, response.getBody().getId());
        assertEquals(500.0, response.getBody().getInvEur());

        verify(transactionService, times(1))
                .createTransaction(any(TransactionDTO.class), eq(itemId), eq("test@example.com"));
    }

    @Test
    @DisplayName("Debería retornar 204 No Content al eliminar una transacción existente")
    void delete_SiExiste_DebeRetornar204() {
        Authentication auth = mock(Authentication.class);
        when(auth.getName()).thenReturn("test@example.com");
        when(transactionService.deleteTransaction(10L, "test@example.com")).thenReturn(true);

        ResponseEntity<Void> response = transactionController.delete(10L, auth);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
    }

    @Test
    @DisplayName("Debería retornar 404 Not Found al eliminar una transacción inexistente")
    void delete_NoExiste_DebeRetornar404() {
        Authentication auth = mock(Authentication.class);
        when(auth.getName()).thenReturn("test@example.com");
        when(transactionService.deleteTransaction(99L, "test@example.com")).thenReturn(false);

        ResponseEntity<Void> response = transactionController.delete(99L, auth);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }
}
