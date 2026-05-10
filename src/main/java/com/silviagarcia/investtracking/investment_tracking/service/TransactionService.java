package com.silviagarcia.investtracking.investment_tracking.service;

import com.silviagarcia.investtracking.investment_tracking.dto.TransactionDTO;
import com.silviagarcia.investtracking.investment_tracking.model.Item;
import com.silviagarcia.investtracking.investment_tracking.model.Transaction;
import com.silviagarcia.investtracking.investment_tracking.model.User;
import com.silviagarcia.investtracking.investment_tracking.repository.ItemRepository;
import com.silviagarcia.investtracking.investment_tracking.repository.TransactionRepository;
import com.silviagarcia.investtracking.investment_tracking.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.Optional;

/**
 * Servicio encargado de gestionar las operaciones financieras individuales.
 */
@Service
public class TransactionService {

    @Autowired private TransactionRepository transactionRepository;
    @Autowired private ItemRepository itemRepository;
    @Autowired private UserRepository userRepository;

    /**
     * Registra una nueva transacción para un ítem determinado.
     * * @param dto Datos de la transacción.
     * @param itemId ID del activo al que se asocia.
     * @param callerEmail Email del usuario autenticado.
     * @return {@link TransactionDTO} de la transacción creada.
     */
    @Transactional
    public TransactionDTO createTransaction(TransactionDTO dto, Long itemId, String callerEmail) {
        if (dto.getStocks() == null || dto.getPurchasePrice() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "stocks y purchasePrice son obligatorios");
        }

        Item item = itemRepository.findById(itemId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Item no encontrado con ID: " + itemId));

        User caller = getUserByEmail(callerEmail);
        if (item.getUser() == null || !item.getUser().getId().equals(caller.getId())) {
            throw new AccessDeniedException("Acceso denegado");
        }

        Transaction tx = new Transaction();
        tx.setStocks(dto.getStocks());
        tx.setPurchasePrice(dto.getPurchasePrice());

        if (dto.getInvEur() == null || dto.getInvEur() == 0) {
            tx.setInvEur(dto.getStocks() * dto.getPurchasePrice());
        } else {
            tx.setInvEur(dto.getInvEur());
        }

        tx.setPurchaseDate(dto.getPurchaseDate() != null ? dto.getPurchaseDate() : LocalDateTime.now());
        tx.setItem(item);

        Transaction savedTx = transactionRepository.save(tx);

        TransactionDTO resultDto = new TransactionDTO();
        resultDto.setId(savedTx.getId());
        resultDto.setStocks(savedTx.getStocks());
        resultDto.setPurchasePrice(savedTx.getPurchasePrice());
        resultDto.setInvEur(savedTx.getInvEur());
        resultDto.setPurchaseDate(savedTx.getPurchaseDate());
        resultDto.setItemId(item.getId());

        return resultDto;
    }
    /**
     * Elimina una transacción verificando la propiedad del activo padre.
     * * @param id Identificador de la transacción.
     * @param callerEmail Email para control de acceso.
     * @return true si el borrado fue exitoso.
     */
    @Transactional
    public boolean deleteTransaction(Long id, String callerEmail) {
        Optional<Transaction> txOpt = transactionRepository.findById(id);
        if (txOpt.isEmpty()) return false;

        Transaction tx = txOpt.get();
        User caller = getUserByEmail(callerEmail);
        if (tx.getItem() == null || tx.getItem().getUser() == null
                || !tx.getItem().getUser().getId().equals(caller.getId())) {
            throw new AccessDeniedException("Acceso denegado");
        }

        transactionRepository.delete(tx);
        return true;
    }
    /** Helper para obtener el usuario autenticado. */
    private User getUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Usuario autenticado no encontrado"));
    }
}

