package com.silviagarcia.investtracking.Investment_Tracking.model;


import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import java.time.LocalDateTime;
import static org.junit.jupiter.api.Assertions.*;

class TransactionModelTest {

    @Test
    @DisplayName("Debería registrar correctamente los valores de la compra")
    void testTransactionData() {
        Transaction t = new Transaction();
        t.setStocks(2.5);
        t.setPurchasePrice(100.0);
        // 2.5 * 100
        t.setInvEur(250.0);
        t.setPurchaseDate(LocalDateTime.now());

        assertEquals(2.5, t.getStocks());
        assertEquals(100.0, t.getPurchasePrice());
        assertEquals(250.0, t.getInvEur());
        assertNotNull(t.getPurchaseDate());
    }
}