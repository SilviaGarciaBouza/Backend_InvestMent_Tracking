package com.silviagarcia.investtracking.Investment_Tracking.controller;

import com.silviagarcia.investtracking.Investment_Tracking.dto.ItemDTO;
import com.silviagarcia.investtracking.Investment_Tracking.service.ItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/// Controlador encargado de gestionar los activos de la cartera.
@RestController
@RequestMapping("/api/items")
@CrossOrigin(origins = "*") // Permite la conexion desde Flutter/Web sin errores de seguridad [cite: 2026-03-08]
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
        // Llama al servicio que crea el Item y la Transaccion inicial [cite: 2026-03-08]
        return itemService.saveItemFromMap(data);
    }

    /**
     * Elimina un activo de la base de datos MariaDB por su ID.
     * DELETE: http://localhost:8080/api/items/{id}
     */
    @DeleteMapping("/{id}")
    public void deleteItem(@PathVariable Long id) {
        itemService.deleteItem(id);
    }
}