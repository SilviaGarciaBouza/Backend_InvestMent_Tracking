package com.silviagarcia.investtracking.Investment_Tracking.controller;

import com.silviagarcia.investtracking.Investment_Tracking.dto.ItemDTO;
import com.silviagarcia.investtracking.Investment_Tracking.service.ItemService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/// Controlador encargado de gestionar los activos de la cartera.
@RestController
@RequestMapping("/api/items")
@CrossOrigin(origins = "*")
public class ItemController {

    @Autowired
    private ItemService itemService;

    /**
     * Obtiene la cartera completa de un usuario especifico.
     * GET: http://localhost:8080/api/items/user/{userId}
     */
    @GetMapping("/user/{userId}")
    public List<ItemDTO> getItems(@PathVariable Long userId) {
        return itemService.getItemsByUserId(userId);
    }

    /**
     * Guarda una nueva inversion enviada desde la App de Flutter.
     * POST: http://localhost:8080/api/items
     */
    @PostMapping
    public ItemDTO saveItem(@RequestBody Map<String, Object> data) {
        return itemService.saveItemFromMap(data);
    }

    /**
     * Elimina un activo de la base de datos MariaDB por su ID.
     * DELETE: http://localhost:8080/api/items/{id}
     */
    @DeleteMapping("/{id}")
    @Transactional
    public ResponseEntity<?> deleteItem(@PathVariable Long id) {
        try {
            boolean deleted = itemService.deleteItemById(id);

            if (deleted) {
                System.out.println("Ítem " + id + " eliminado correctamente de PostgreSQL");
                return ResponseEntity.ok().build();
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            System.err.println("Error al borrar ítem: " + e.getMessage());
            return ResponseEntity.internalServerError().build();
        }
    }
}