package com.silviagarcia.investtracking.investment_tracking.repository;

import com.silviagarcia.investtracking.investment_tracking.model.Item;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

/**
 * Repositorio para la gestión de activos financieros (Items).
 * Centraliza las consultas de la cartera de inversión de los usuarios.
 */
@Repository
public interface ItemRepository extends JpaRepository<Item, Long> {

    /**
     * Recupera todos los activos de un usuario cargando sus transacciones de forma eficiente.
     * Utiliza 'FETCH' para inicializar la colección de transacciones en una sola consulta,
     * evitando problemas de rendimiento (N+1 selects).
     * * @param userId Identificador único del usuario.
     * @return Lista de activos con sus transacciones ya inicializadas.
     */
    @Query("SELECT i FROM Item i LEFT JOIN FETCH i.transactions WHERE i.user.id = :userId")
    List<Item> findByUserIdWithTransactions(@Param("userId") Long userId);
}
