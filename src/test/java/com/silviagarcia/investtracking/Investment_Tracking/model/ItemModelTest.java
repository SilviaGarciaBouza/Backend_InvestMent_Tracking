package com.silviagarcia.investtracking.Investment_Tracking.model;


import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import java.util.ArrayList;
import static org.junit.jupiter.api.Assertions.*;

class ItemModelTest {

    @Test
    @DisplayName("Debería vincular el activo con su dueño y su tipo")
    void testItemRelationships() {
        User user = new User();
        Category cat = new Category();
        cat.setName("Cripto");

        Item item = new Item();
        item.setName("Bitcoin");
        item.setUser(user);
        item.setCategory(cat);
        item.setTransactions(new ArrayList<>());

        assertEquals("Bitcoin", item.getName());
        assertNotNull(item.getUser());
        assertEquals("Cripto", item.getCategory().getName());
        assertTrue(item.getTransactions().isEmpty());
    }
}