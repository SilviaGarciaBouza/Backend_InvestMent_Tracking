package com.silviagarcia.investtracking.Investment_Tracking.repository;

import com.silviagarcia.investtracking.Investment_Tracking.model.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

/**
 * Repositorio para el historial de transacciones de inversión.
 * Gestiona la persistencia de los movimientos financieros de cada activo.
 */
@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {

    /**
     * Busca todas las transacciones asociadas a un ítem o activo específico.
     * @param itemId Identificador del activo padre.
     * @return Lista de transacciones del activo.
     */
    List<Transaction> findByItemId(Long itemId);
}