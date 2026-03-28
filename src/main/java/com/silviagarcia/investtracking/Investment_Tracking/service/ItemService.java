package com.silviagarcia.investtracking.Investment_Tracking.service;

import com.silviagarcia.investtracking.Investment_Tracking.dto.*;
import com.silviagarcia.investtracking.Investment_Tracking.model.*;
import com.silviagarcia.investtracking.Investment_Tracking.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Servicio central para la gestión de activos e inversiones.
 * Maneja la lógica de persistencia híbrida y la integración de precios externos.
 */
@Service
public class ItemService {

    @Autowired private ItemRepository itemRepository;
    @Autowired private UserRepository userRepository;
    @Autowired private CategoryRepository categoryRepository;
    @Autowired private TransactionRepository transactionRepository;
    @Autowired private PriceService priceService;

    /**
     * Crea un activo y registra su primera transacción de compra.
     * @param data Mapa con datos del activo e inversión inicial.
     * @return {@link ItemDTO} con los datos persistidos.
     */
    @Transactional
    public ItemDTO saveItemFromMap(Map<String, Object> data) {
        Item item = new Item();
        item.setName((String) data.get("name"));

        Long userId = ((Number) data.get("userId")).longValue();
        Long catId = ((Number) data.get("categoryId")).longValue();

        item.setUser(userRepository.findById(userId).orElseThrow(() -> new RuntimeException("Usuario no encontrado")));
        item.setCategory(categoryRepository.findById(catId).orElseThrow(() -> new RuntimeException("Categoría no encontrada")));

        Item savedItem = itemRepository.save(item);

        Transaction tx = new Transaction();
        tx.setItem(savedItem);
        tx.setStocks(((Number) data.get("initialStocks")).doubleValue());
        tx.setPurchasePrice(((Number) data.get("initialPrice")).doubleValue());
        tx.setInvEur(tx.getStocks() * tx.getPurchasePrice());
        tx.setPurchaseDate(LocalDateTime.now());

        transactionRepository.save(tx);
        return convertToDTO(savedItem);
    }

    @Transactional(readOnly = true)
    public List<ItemDTO> getItemsByUserId(Long userId) {
        return itemRepository.findByUserId(userId).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Transactional
    public boolean deleteItemById(Long id) {
        if (itemRepository.existsById(id)) {
            itemRepository.deleteById(id);
            return true;
        }
        return false;
    }

    /**
     * Convierte una entidad Item a su representación DTO.
     * Calcula el precio actual y mapea las transacciones incluyendo el ID de referencia.
     */
    private ItemDTO convertToDTO(Item item) {
        ItemDTO itemDto = new ItemDTO();
        itemDto.setId(item.getId());
        itemDto.setName(item.getName());

        Double price = priceService.getRealTimePrice(item.getName());
        if ((price == null || price == 0.0) && item.getTransactions() != null && !item.getTransactions().isEmpty()) {
            price = item.getTransactions().get(item.getTransactions().size() - 1).getPurchasePrice();
        }
        itemDto.setCurrentPrice(price);

        if (item.getCategory() != null) {
            itemDto.setCategory(new CategoryDTO(item.getCategory().getId(), item.getCategory().getName()));
        }

        if (item.getTransactions() != null) {
            itemDto.setTransactions(item.getTransactions().stream().map(tx -> {
                TransactionDTO txDto = new TransactionDTO();
                txDto.setId(tx.getId());
                txDto.setStocks(tx.getStocks());
                txDto.setPurchasePrice(tx.getPurchasePrice());
                txDto.setInvEur(tx.getInvEur());
                txDto.setPurchaseDate(tx.getPurchaseDate());
                txDto.setItemId(item.getId());
                return txDto;
            }).collect(Collectors.toList()));
        }
        return itemDto;
    }
}