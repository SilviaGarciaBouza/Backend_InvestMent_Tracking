package com.silviagarcia.investtracking.investment_tracking.controller;

import com.silviagarcia.investtracking.investment_tracking.dto.ItemDTO;
import com.silviagarcia.investtracking.investment_tracking.service.ItemService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ItemControllerTest {

    @Mock private ItemService itemService;
    @InjectMocks private ItemController itemController;

    @Test
    void saveItem_DebeRetornarStatusCreated() {
        Map<String, Object> data = Map.of("name", "Apple", "userId", 1);
        ItemDTO dto = new ItemDTO();
        dto.setName("Apple");

        Authentication auth = mock(Authentication.class);
        when(auth.getName()).thenReturn("test@example.com");
        when(itemService.saveItemFromMap(data, "test@example.com")).thenReturn(dto);

        ResponseEntity<ItemDTO> response = itemController.saveItem(data, auth);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals("Apple", response.getBody().getName());
    }

    @Test
    void deleteItem_SiExiste_DebeRetornarOk() {
        Authentication auth = mock(Authentication.class);
        when(auth.getName()).thenReturn("test@example.com");
        when(itemService.deleteItemById(1L, "test@example.com")).thenReturn(true);

        ResponseEntity<?> response = itemController.deleteItem(1L, auth);

        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void getItems_DebeRetornarLista() {
        Authentication auth = mock(Authentication.class);
        when(auth.getName()).thenReturn("test@example.com");

        ItemDTO dto = new ItemDTO();
        dto.setName("Bitcoin");
        when(itemService.getItemsByUserId(1L, "test@example.com")).thenReturn(java.util.List.of(dto));

        java.util.List<ItemDTO> result = itemController.getItems(1L, auth);

        assertEquals(1, result.size());
        assertEquals("Bitcoin", result.get(0).getName());
    }

    @Test
    void deleteItem_NoExiste_DebeRetornar404() {
        Authentication auth = mock(Authentication.class);
        when(auth.getName()).thenReturn("test@example.com");
        when(itemService.deleteItemById(99L, "test@example.com")).thenReturn(false);

        ResponseEntity<?> response = itemController.deleteItem(99L, auth);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }
}
