package com.silviagarcia.investtracking.investment_tracking.controller;

import com.silviagarcia.investtracking.investment_tracking.dto.ItemDTO;
;
import com.silviagarcia.investtracking.investment_tracking.service.ItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;

/**
 * Controlador para la gestión de activos financieros (Items).
 * Coordina la visualización de la cartera y la sincronización con el cliente móvil.
 */
@RestController
@RequestMapping("/api/items")
@CrossOrigin(origins = "*")
public class ItemController {

    @Autowired
    private ItemService itemService;

    @GetMapping("/user/{userId}")
    public List<ItemDTO> getItems(@PathVariable Long userId, Authentication auth) {
        return itemService.getItemsByUserId(userId, auth.getName());
    }

    @PostMapping
    public ResponseEntity<ItemDTO> saveItem(@RequestBody Map<String, Object> data, Authentication auth) {
        ItemDTO saved = itemService.saveItemFromMap(data, auth.getName());
        return new ResponseEntity<>(saved, HttpStatus.CREATED);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteItem(@PathVariable Long id, Authentication auth) {
        boolean deleted = itemService.deleteItemById(id, auth.getName());
        if (deleted) {
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
