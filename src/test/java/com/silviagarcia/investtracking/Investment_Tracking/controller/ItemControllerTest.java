package com.silviagarcia.investtracking.Investment_Tracking.controller;


import com.silviagarcia.investtracking.Investment_Tracking.dto.ItemDTO;
import com.silviagarcia.investtracking.Investment_Tracking.service.ItemService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ItemControllerTest {

    @Mock private ItemService itemService;
    @InjectMocks private ItemController itemController;

    @Test
    void saveItem_DebeRetornarStatusCreated() {
        // Arrange
        Map<String, Object> data = Map.of("name", "Apple", "userId", 1);
        ItemDTO dto = new ItemDTO();
        dto.setName("Apple");

        when(itemService.saveItemFromMap(data)).thenReturn(dto);

        // Act
        ResponseEntity<ItemDTO> response = itemController.saveItem(data);

        // Assert
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals("Apple", response.getBody().getName());
    }

    @Test
    void deleteItem_SiExiste_DebeRetornarOk() {
        // Arrange
        when(itemService.deleteItemById(1L)).thenReturn(true);

        // Act
        ResponseEntity<?> response = itemController.deleteItem(1L);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }
}
