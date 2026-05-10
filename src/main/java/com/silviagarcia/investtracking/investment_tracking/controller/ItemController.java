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
 * Controlador REST para la gestión de activos financieros (Items).
 * Coordina las operaciones de visualización, creación y eliminación de la cartera
 * de inversiones, asegurando la sincronización de datos entre el servidor y el cliente móvil.
 */
@RestController
@RequestMapping("/api/items")
@CrossOrigin(origins = "*")
public class ItemController {

    @Autowired
    private ItemService itemService;

    /**
     * Obtiene la lista completa de activos pertenecientes a un usuario específico.
     * * @param userId Identificador único del usuario propietario de los activos.
     * @param auth Objeto de autenticación de Spring Security para validar la identidad del solicitante.
     * @return Lista de {@link ItemDTO} con la información detallada de cada activo.
     */
    @GetMapping("/user/{userId}")
    public List<ItemDTO> getItems(@PathVariable Long userId, Authentication auth) {
        return itemService.getItemsByUserId(userId, auth.getName());
    }

    /**
     * Registra un nuevo activo o una nueva transacción en la base de datos.
     * * @param data Mapa de datos que contiene la información del activo (nombre, cantidad, precio, categoría).
     * @param auth Objeto de autenticación para vincular el activo al usuario actualmente logueado.
     * @return {@link ResponseEntity} con el {@link ItemDTO} creado y el estado HTTP 201 (Created).
     */
    @PostMapping
    public ResponseEntity<ItemDTO> saveItem(@RequestBody Map<String, Object> data, Authentication auth) {
        ItemDTO saved = itemService.saveItemFromMap(data, auth.getName());
        return new ResponseEntity<>(saved, HttpStatus.CREATED);
    }

    /**
     * Elimina un activo de la base de datos mediante su identificador.
     * * @param id Identificador único del activo a eliminar.
     * @param auth Objeto de autenticación para verificar que el usuario es dueño del activo antes de borrar.
     * @return {@link ResponseEntity} con estado 200 (OK) si se eliminó, o 404 (Not Found) si no se encontró.
     */
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
