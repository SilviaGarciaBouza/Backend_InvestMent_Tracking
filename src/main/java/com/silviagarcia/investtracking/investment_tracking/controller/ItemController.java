package com.silviagarcia.investtracking.investment_tracking.controller;

import com.silviagarcia.investtracking.investment_tracking.dto.ItemDTO;
import com.silviagarcia.investtracking.investment_tracking.service.ItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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

    /**
     * Obtiene la cartera de activos de un usuario.
     * @param userId ID del usuario propietario.
     * @return Lista de activos vinculados.
     */
    @GetMapping("/user/{userId}")
    public List<ItemDTO> getItems(@PathVariable Long userId) {
        return itemService.getItemsByUserId(userId);
    }

    /**
     * Crea un nuevo activo a partir de un mapa de datos (JSON flexible).
     * @param data Mapa con la información del activo y sus relaciones.
     * @return El DTO del activo creado con estado 201.
     */
    @PostMapping
    public ResponseEntity<ItemDTO> saveItem(@RequestBody Map<String, Object> data) {
        ItemDTO saved = itemService.saveItemFromMap(data);
        return new ResponseEntity<>(saved, HttpStatus.CREATED);
    }

    /**
     * Elimina un activo por su identificador único.
     * @param id Identificador del activo a borrar.
     * @return Respuesta HTTP indicando éxito o error.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteItem(@PathVariable Long id) {
        try {
            boolean deleted = itemService.deleteItemById(id);
            if (deleted) {
                return ResponseEntity.ok().build();
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al borrar: " + e.getMessage());
        }
    }
}