package com.silviagarcia.investtracking.Investment_Tracking.repository;

import com.silviagarcia.investtracking.Investment_Tracking.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repositorio para la gestión de categorías .
 * Permite realizar operaciones CRUD sobre la tabla 'categories'.
 */
@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {
}