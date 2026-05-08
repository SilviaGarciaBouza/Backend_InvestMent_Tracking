package com.silviagarcia.investtracking.investment_tracking.service;

import com.silviagarcia.investtracking.investment_tracking.dto.*;
import com.silviagarcia.investtracking.investment_tracking.model.*;
import com.silviagarcia.investtracking.investment_tracking.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Servicio central para la gestión de activos e inversiones.
 */
@Service
public class ItemService {

    @Autowired private ItemRepository itemRepository;
    @Autowired private UserRepository userRepository;
    @Autowired private CategoryRepository categoryRepository;
    @Autowired private TransactionRepository transactionRepository;
    @Autowired private PriceService priceService;

    @Transactional
    public ItemDTO saveItemFromMap(Map<String, Object> data, String callerEmail) {
        Object nameObj = data.get("name");
        Object userIdObj = data.get("userId");
        Object catIdObj = data.get("categoryId");

        if (nameObj == null || userIdObj == null || catIdObj == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "name, userId y categoryId son obligatorios");
        }

        Long userId = ((Number) userIdObj).longValue();
        Long catId = ((Number) catIdObj).longValue();

        User caller = getUserByEmail(callerEmail);
        if (!caller.getId().equals(userId)) {
            throw new AccessDeniedException("Acceso denegado");
        }

        Item item = new Item();
        item.setName((String) nameObj);
        item.setUser(userRepository.findById(userId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuario no encontrado")));
        item.setCategory(categoryRepository.findById(catId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Categoría no encontrada")));

        Item savedItem = itemRepository.save(item);
        return convertToDTO(savedItem, Map.of());
    }

    public List<ItemDTO> getItemsByUserId(Long userId, String callerEmail) {
        User caller = getUserByEmail(callerEmail);
        if (!caller.getId().equals(userId)) {
            throw new AccessDeniedException("Acceso denegado");
        }

        List<Item> items = itemRepository.findByUserIdWithTransactions(userId);
        if (items.isEmpty()) return List.of();

        String symbols = items.stream()
                .map(Item::getName)
                .distinct()
                .collect(Collectors.joining(","));

        Map<String, Object> priceMap = priceService.getBatchPrices(symbols);

        return items.stream()
                .map(item -> convertToDTO(item, priceMap))
                .collect(Collectors.toList());
    }

    @Transactional
    public boolean deleteItemById(Long id, String callerEmail) {
        Item item = itemRepository.findById(id).orElse(null);
        if (item == null) return false;

        User caller = getUserByEmail(callerEmail);
        if (item.getUser() == null || !item.getUser().getId().equals(caller.getId())) {
            throw new AccessDeniedException("Acceso denegado");
        }

        itemRepository.delete(item);
        return true;
    }

    private User getUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Usuario autenticado no encontrado"));
    }

    private ItemDTO convertToDTO(Item item, Map<String, Object> priceMap) {
        ItemDTO itemDto = new ItemDTO();
        itemDto.setId(item.getId());
        itemDto.setName(item.getName());

        Double price = 0.0;

        if (priceMap != null && priceMap.containsKey(item.getName())) {
            Object data = priceMap.get(item.getName());
            if (data instanceof Map) {
                Object p = ((Map<?, ?>) data).get("price");
                if (p != null) price = Double.parseDouble(p.toString());
            }
        }

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
