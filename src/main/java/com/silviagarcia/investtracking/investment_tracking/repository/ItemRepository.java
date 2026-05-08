package com.silviagarcia.investtracking.investment_tracking.repository;

import com.silviagarcia.investtracking.investment_tracking.model.Item;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

/**
 * Repositorio para la gestión de activos financieros (Items).
 */
@Repository
public interface ItemRepository extends JpaRepository<Item, Long> {

    /**
     * Recupera una lista de activos pertenecientes a un usuario específico.
     * @param userId Identificador único del usuario.
     * @return Lista de activos del usuario.
     */
    @Query("SELECT i FROM Item i LEFT JOIN FETCH i.transactions WHERE i.user.id = :userId")
    List<Item> findByUserIdWithTransactions(@Param("userId") Long userId);
}
