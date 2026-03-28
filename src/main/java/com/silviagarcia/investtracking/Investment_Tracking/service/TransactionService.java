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

/**
 * Servicio encargado de gestionar las operaciones financieras individuales.
 * <p>
 * Permite registrar nuevas compras o movimientos asociados a un activo (Item)
 * específico, garantizando la integridad de los datos en MariaDB.
 */
@Service
public class TransactionService {

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private ItemRepository itemRepository;

    /**
     * Registra una nueva transacción y la vincula a un activo existente.
     * <p>
     * Este método es transaccional; si ocurre un error al guardar, se revertirán
     * todos los cambios para evitar datos huérfanos.
     * * @param dto    Objeto de transporte con los datos de la transacción (stocks, precio, etc).
     * @param itemId Identificador único del activo al que se asocia la operación.
     * @return {@link TransactionDTO} que incluye el ID generado por la base de datos.
     * @throws RuntimeException si el ítem padre no existe en el sistema.
     */
    @Transactional
    public TransactionDTO createTransaction(TransactionDTO dto, Long itemId) {
        // Localizo el Item (name es como símbolo para cotizaciones)
        Item item = itemRepository.findById(itemId)
                .orElseThrow(() -> new RuntimeException("Item no encontrado con ID: " + itemId));

        Transaction tx = new Transaction();
        tx.setStocks(dto.getStocks());
        tx.setPurchasePrice(dto.getPurchasePrice());

        // Si el frontend no envía el total, lo calculo automaaticamente
        if (dto.getInvEur() == null || dto.getInvEur() == 0) {
            tx.setInvEur(dto.getStocks() * dto.getPurchasePrice());
        } else {
            tx.setInvEur(dto.getInvEur());
        }

        tx.setPurchaseDate(dto.getPurchaseDate() != null ? dto.getPurchaseDate() : LocalDateTime.now());
        tx.setItem(item);

        // Persistencia en MariaDB
        Transaction savedTx = transactionRepository.save(tx);

        // Mapeo manual al DTO de respuesta
        TransactionDTO resultDto = new TransactionDTO();
        resultDto.setId(savedTx.getId());
        resultDto.setStocks(savedTx.getStocks());
        resultDto.setPurchasePrice(savedTx.getPurchasePrice());
        resultDto.setInvEur(savedTx.getInvEur());
        resultDto.setPurchaseDate(savedTx.getPurchaseDate());
        resultDto.setItemId(item.getId());

        return resultDto;
    }
}