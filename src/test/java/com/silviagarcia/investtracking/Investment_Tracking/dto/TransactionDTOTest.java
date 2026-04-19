package com.silviagarcia.investtracking.Investment_Tracking.dto;


import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import java.time.LocalDateTime;
import static org.junit.jupiter.api.Assertions.*;

class TransactionDTOTest {

    @Test
    @DisplayName("Debería transportar los datos financieros y el ID del ítem padre")
    void testTransactionDTO() {
        LocalDateTime ahora = LocalDateTime.now();
        TransactionDTO dto = new TransactionDTO(50L, 0.5, 60000.0, 30000.0, ahora, 10L);

        assertEquals(0.5, dto.getStocks());
        assertEquals(30000.0, dto.getInvEur());
        assertEquals(10L, dto.getItemId(), "El itemId debe ser correcto para el mapeo en el móvil");
        assertEquals(ahora, dto.getPurchaseDate());
    }
}
