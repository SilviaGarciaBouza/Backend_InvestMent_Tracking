package com.silviagarcia.investtracking.Investment_Tracking.controller;

import com.silviagarcia.investtracking.Investment_Tracking.dto.TransactionDTO;
import com.silviagarcia.investtracking.Investment_Tracking.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Controlador para el registro de operaciones (compras/ventas) individuales.
 */
@RestController
@RequestMapping("/api/transactions")
@CrossOrigin(origins = "*")
public class TransactionController {

    @Autowired
    private TransactionService transactionService;

    /**
     * Registra una nueva transacción para un activo existente.
     * @param dto Datos de la operación.
     * @param itemId ID del ítem al que se vincula.
     * @return La transacción creada.
     */
    @PostMapping("/item/{itemId}")
    public ResponseEntity<TransactionDTO> create(@RequestBody TransactionDTO dto, @PathVariable Long itemId) {
        TransactionDTO created = transactionService.createTransaction(dto, itemId);
        return new ResponseEntity<>(created, HttpStatus.CREATED);
    }
}