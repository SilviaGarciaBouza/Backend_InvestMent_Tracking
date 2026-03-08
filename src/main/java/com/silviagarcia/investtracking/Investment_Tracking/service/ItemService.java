package com.silviagarcia.investtracking.Investment_Tracking.service;

import com.silviagarcia.investtracking.Investment_Tracking.dto.CategoryDTO;
import com.silviagarcia.investtracking.Investment_Tracking.dto.ItemDTO;
import com.silviagarcia.investtracking.Investment_Tracking.dto.TransactionDTO;
import com.silviagarcia.investtracking.Investment_Tracking.model.*;
import com.silviagarcia.investtracking.Investment_Tracking.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/// Servicio principal para la gestión de la cartera de inversiones.
@Service
public class ItemService {

    @Autowired
    private ItemRepository itemRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private TransactionRepository transactionRepository;

    /**
     * Crea un nuevo activo y su transacción inicial obligatoria.
     * Sin la transacción, el valor en Flutter siempre sería 0.00€. [cite: 2026-03-08]
     */
    @Transactional
    public ItemDTO saveItemFromMap(Map<String, Object> data) {
        // 1. Crear y configurar la entidad Item
        Item item = new Item();
        item.setName((String) data.get("name"));

        // Extraer IDs y buscar entidades relacionadas
        Long userId = ((Number) data.get("userId")).longValue();
        Long catId = ((Number) data.get("categoryId")).longValue();

        item.setUser(userRepository.findById(userId).orElse(null));
        item.setCategory(categoryRepository.findById(catId).orElse(null));

        // Guardar el activo en MariaDB
        Item savedItem = itemRepository.save(item);

        // 2. Crear la transacción inicial para que la App tenga datos que sumar [cite: 2026-03-08]
        Transaction tx = new Transaction();
        tx.setItem(savedItem);
        tx.setStocks(((Number) data.get("initialStocks")).doubleValue());
        tx.setPurchasePrice(((Number) data.get("initialPrice")).doubleValue());

        // Cálculo de inversión: $$invEur = stocks \times purchasePrice$$
        tx.setInvEur(tx.getStocks() * tx.getPurchasePrice());
        tx.setPurchaseDate(LocalDateTime.now());

        transactionRepository.save(tx);

        // Retornar el activo convertido a DTO para la respuesta de la API
        return convertToDTO(savedItem);
    }

    /// Elimina el activo de MariaDB por su identificador único.
    public void deleteItem(Long id) {
        itemRepository.deleteById(id);
    }

    /// Obtiene todos los activos de un usuario específico.
    public List<ItemDTO> getItemsByUserId(Long userId) {
        List<Item> items = itemRepository.findByUserId(userId);
        return items.stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    /**
     * Transforma una entidad Item en un DTO, incluyendo categorías y transacciones.
     * Esto evita recursividad infinita en el JSON. [cite: 2026-03-08]
     */
    private ItemDTO convertToDTO(Item item) {
        ItemDTO itemDto = new ItemDTO();
        itemDto.setId(item.getId());
        itemDto.setName(item.getName());

        // Mapeo de Categoría
        if (item.getCategory() != null) {
            CategoryDTO catDto = new CategoryDTO();
            catDto.setId(item.getCategory().getId());
            catDto.setName(item.getCategory().getName());
            itemDto.setCategory(catDto);
        }

        // Mapeo de lista de Transacciones (Crucial para los cálculos en Flutter)
        if (item.getTransactions() != null) {
            List<TransactionDTO> txDtos = item.getTransactions().stream().map(tx -> {
                TransactionDTO txDto = new TransactionDTO();
                txDto.setId(tx.getId());
                txDto.setStocks(tx.getStocks());
                txDto.setPurchasePrice(tx.getPurchasePrice());
                txDto.setInvEur(tx.getInvEur());
                txDto.setPurchaseDate(tx.getPurchaseDate());
                return txDto;
            }).collect(Collectors.toList());
            itemDto.setTransactions(txDtos);
        }

        return itemDto;
    }
}