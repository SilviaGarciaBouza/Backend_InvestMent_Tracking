package com.silviagarcia.investtracking.investment_tracking.repository;

import com.silviagarcia.investtracking.investment_tracking.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repositorio para la gestión de categorías .
 * Permite realizar operaciones CRUD sobre la tabla 'categories'.
 */
@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {
}