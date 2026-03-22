package com.silviagarcia.investtracking.Investment_Tracking.service;

import com.silviagarcia.investtracking.Investment_Tracking.dto.TransactionDTO;
import com.silviagarcia.investtracking.Investment_Tracking.model.Item;
import com.silviagarcia.investtracking.Investment_Tracking.model.Transaction;
import com.silviagarcia.investtracking.Investment_Tracking.repository.ItemRepository;
import com.silviagarcia.investtracking.Investment_Tracking.repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

/// Servicio para gestionar las operaciones de compra (transacciones) individuales.
@Service
public class TransactionService {

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private ItemRepository itemRepository;

    /// Registra una nueva transacción y la vincula a un activo existente.
    /// @param dto Datos de la transacción provenientes del frontend.
    /// @param itemId ID del activo al que pertenece la transacción.
    /// @return El DTO de la transacción guardada.
    @Transactional
    public TransactionDTO createTransaction(TransactionDTO dto, Long itemId) {
        Item item = itemRepository.findById(itemId)
                .orElseThrow(() -> new RuntimeException("Item no encontrado con ID: " + itemId));

        Transaction tx = new Transaction();
        tx.setStocks(dto.getStocks());
        tx.setPurchasePrice(dto.getPurchasePrice());
        tx.setInvEur(dto.getInvEur());
        tx.setPurchaseDate(dto.getPurchaseDate() != null ? dto.getPurchaseDate() : LocalDateTime.now());

        tx.setItem(item);

        Transaction savedTx = transactionRepository.save(tx);

        TransactionDTO resultDto = new TransactionDTO();
        resultDto.setId(savedTx.getId());
        resultDto.setStocks(savedTx.getStocks());
        resultDto.setPurchasePrice(savedTx.getPurchasePrice());
        resultDto.setInvEur(savedTx.getInvEur());
        resultDto.setPurchaseDate(savedTx.getPurchaseDate());

        return resultDto;
    }
}