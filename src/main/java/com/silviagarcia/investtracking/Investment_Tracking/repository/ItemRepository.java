package com.silviagarcia.investtracking.Investment_Tracking.repository;

import com.silviagarcia.investtracking.Investment_Tracking.model.User;
import com.silviagarcia.investtracking.Investment_Tracking.model.Category;
import com.silviagarcia.investtracking.Investment_Tracking.model.Item;
import com.silviagarcia.investtracking.Investment_Tracking.model.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;
import java.util.List;



/// Repositorio para la gestión de activos financieros.
public interface ItemRepository extends JpaRepository<Item, Long> {
    List<Item> findByUserId(Long userId);
}
