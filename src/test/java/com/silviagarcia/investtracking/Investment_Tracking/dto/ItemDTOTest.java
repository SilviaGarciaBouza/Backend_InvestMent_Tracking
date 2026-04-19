package com.silviagarcia.investtracking.Investment_Tracking.dto;


import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

class ItemDTOTest {

    @Test
    @DisplayName("Debería manejar correctamente la estructura anidada y el precio calculado")
    void testItemDTOStructure() {
        CategoryDTO catDto = new CategoryDTO(1L, "Cripto");
        TransactionDTO transDto = new TransactionDTO();
        transDto.setInvEur(500.0);

        ItemDTO dto = new ItemDTO(
                100L,
                "Bitcoin",
                catDto,
                List.of(transDto),
                70000.0
        );

        assertEquals("Bitcoin", dto.getName());
        assertEquals("Cripto", dto.getCategory().getName());
        assertEquals(1, dto.getTransactions().size());
        assertEquals(70000.0, dto.getCurrentPrice(), "El precio actual debe poder transportarse al frontend");
    }
}
