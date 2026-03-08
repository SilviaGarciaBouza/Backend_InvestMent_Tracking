package com.silviagarcia.investtracking.Investment_Tracking.service;

import com.silviagarcia.investtracking.Investment_Tracking.dto.CategoryDTO;
import com.silviagarcia.investtracking.Investment_Tracking.dto.ItemDTO;
import com.silviagarcia.investtracking.Investment_Tracking.dto.TransactionDTO;
import com.silviagarcia.investtracking.Investment_Tracking.model.Item;
import com.silviagarcia.investtracking.Investment_Tracking.repository.ItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/// Servicio principal para la gestión de la cartera de inversiones.
@Service
public class ItemService {

    @Autowired
    private ItemRepository itemRepository;

    /// Obtiene todos los activos de un usuario específico.
    /// @param userId ID del usuario propietario de los activos.
    /// @return Lista de ItemDTO con sus transacciones incluidas.
    public List<ItemDTO> getItemsByUserId(Long userId) {
        List<Item> items = itemRepository.findByUserId(userId);
        return items.stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    /// Método privado para transformar una entidad compleja en un DTO sencillo.
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

        // Mapeo de lista de Transacciones
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