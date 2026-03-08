package com.silviagarcia.investtracking.Investment_Tracking.controller;

import com.silviagarcia.investtracking.Investment_Tracking.dto.ItemDTO;
import com.silviagarcia.investtracking.Investment_Tracking.service.ItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

/// Controlador encargado de gestionar los activos de la cartera.
@RestController
@RequestMapping("/api/items")
@CrossOrigin(origins = "*")
public class ItemController {

    @Autowired
    private ItemService itemService;

    /// Obtiene la cartera completa de un usuario específico.
    /// URL de ejemplo: http://localhost:8080/api/items/user/1
    @GetMapping("/user/{userId}")
    public List<ItemDTO> getItems(@PathVariable Long userId) {
        return itemService.getItemsByUserId(userId);
    }
}