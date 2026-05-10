package com.silviagarcia.investtracking.investment_tracking.repository;

import com.silviagarcia.investtracking.investment_tracking.model.Transaction;
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
     * Obtiene el historial completo de movimientos para un activo específico.
     * * @param itemId Identificador del activo padre.
     * @return Lista de transacciones ordenadas por pertenencia al activo.
     */
    List<Transaction> findByItemId(Long itemId);
}