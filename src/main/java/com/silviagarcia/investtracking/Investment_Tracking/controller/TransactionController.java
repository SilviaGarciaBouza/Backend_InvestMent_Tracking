package com.silviagarcia.investtracking.Investment_Tracking.controller;

import com.silviagarcia.investtracking.Investment_Tracking.dto.TransactionDTO;
import com.silviagarcia.investtracking.Investment_Tracking.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/// Controlador para registrar nuevas operaciones financieras.
@RestController
@RequestMapping("/api/transactions")
@CrossOrigin(origins = "*")
public class TransactionController {

    @Autowired
    private TransactionService transactionService;

    /// Registra una nueva transacción vinculada a un item.
    @PostMapping("/item/{itemId}")
    public TransactionDTO create(@RequestBody TransactionDTO dto, @PathVariable Long itemId) {
        return transactionService.createTransaction(dto, itemId);
    }
}