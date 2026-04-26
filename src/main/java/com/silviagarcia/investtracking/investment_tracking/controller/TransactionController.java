package com.silviagarcia.investtracking.investment_tracking.controller;

import com.silviagarcia.investtracking.investment_tracking.dto.TransactionDTO;
import com.silviagarcia.investtracking.investment_tracking.service.TransactionService;
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
    /**
     * Elimina una transacción específica del sistema.
     * @param id ID de la transacción a eliminar.
     * @return Respuesta vacía con estado 204 si tiene éxito, o 404 si no existe.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        boolean deleted = transactionService.deleteTransaction(id);
        if (deleted) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}