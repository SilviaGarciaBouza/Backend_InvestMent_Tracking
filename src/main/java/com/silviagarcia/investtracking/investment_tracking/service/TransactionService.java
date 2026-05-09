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
 * <p>
 * Permite registrar nuevas compras o movimientos asociados a un activo (Item)
 * específico, garantizando la integridad de los datos en MariaDB.
 */
@Service
public class TransactionService {

    @Autowired private TransactionRepository transactionRepository;
    @Autowired private ItemRepository itemRepository;
    @Autowired private UserRepository userRepository;

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
     * Elimina una transacción específica de la base de datos.
     *
     * Se verifica la existencia del registro antes de intentar el borrado para
     * asegurar una respuesta coherente al controlador. La anotación @Transactional
     * garantiza que la operación se consolide correctamente en MariaDB.
     *
     * @param id Identificador único de la transacción a eliminar.
     * @return true si la transacción existía y fue eliminada; false si no se encontró.
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

    private User getUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Usuario autenticado no encontrado"));
    }
}

