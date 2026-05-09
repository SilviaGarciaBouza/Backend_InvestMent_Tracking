package com.silviagarcia.investtracking.investment_tracking.service;

import com.silviagarcia.investtracking.investment_tracking.dto.ItemDTO;
import com.silviagarcia.investtracking.investment_tracking.model.Category;
import com.silviagarcia.investtracking.investment_tracking.model.Item;
import com.silviagarcia.investtracking.investment_tracking.model.User;
import com.silviagarcia.investtracking.investment_tracking.repository.CategoryRepository;
import com.silviagarcia.investtracking.investment_tracking.repository.ItemRepository;
import com.silviagarcia.investtracking.investment_tracking.repository.TransactionRepository;
import com.silviagarcia.investtracking.investment_tracking.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ItemServiceTest {

    @Mock private ItemRepository itemRepository;
    @Mock private UserRepository userRepository;
    @Mock private CategoryRepository categoryRepository;
    @Mock private TransactionRepository transactionRepository;
    @Mock private PriceService priceService;

    @InjectMocks private ItemService itemService;

    @Test
    void testSaveItemFromMap_ShouldCreateItem() {
        Map<String, Object> data = Map.of(
                "name", "Bitcoin",
                "userId", 1,
                "categoryId", 1
        );

        User user = new User();
        user.setId(1L);
        Category cat = new Category();
        cat.setId(1L);

        when(userRepository.findByEmail("test@example.com")).thenReturn(Optional.of(user));
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(categoryRepository.findById(1L)).thenReturn(Optional.of(cat));
        when(itemRepository.save(any(Item.class))).thenAnswer(i -> {
            Item it = (Item) i.getArguments()[0];
            it.setId(100L);
            return it;
        });

        var result = itemService.saveItemFromMap(data, "test@example.com");

        assertNotNull(result);
        assertEquals("Bitcoin", result.getName());
        verify(itemRepository).save(any(Item.class));
    }

    @Test
    void testSaveItemFromMap_ShouldThrow400_WhenRequiredFieldsMissing() {
        Map<String, Object> data = Map.of("name", "Bitcoin"); // missing userId and categoryId

        assertThrows(ResponseStatusException.class, () ->
                itemService.saveItemFromMap(data, "test@example.com"));
    }

    @Test
    void testSaveItemFromMap_ShouldThrowAccessDenied_WhenCallerIsNotOwner() {
        Map<String, Object> data = Map.of("name", "Bitcoin", "userId", 1, "categoryId", 1);

        User caller = new User();
        caller.setId(2L); // different from userId 1L

        when(userRepository.findByEmail("other@example.com")).thenReturn(Optional.of(caller));

        assertThrows(AccessDeniedException.class, () ->
                itemService.saveItemFromMap(data, "other@example.com"));
    }

    @Test
    void testGetItemsByUserId_ShouldReturnDTOsWithPrice() {
        User user = new User();
        user.setId(1L);

        Category cat = new Category();
        cat.setId(1L);
        cat.setName("Cripto");

        Item item = new Item();
        item.setId(10L);
        item.setName("BTCUSDT");
        item.setUser(user);
        item.setCategory(cat);
        item.setTransactions(new ArrayList<>());

        Map<String, String> priceData = new HashMap<>();
        priceData.put("price", "60000.0");
        Map<String, Object> priceMap = new HashMap<>();
        priceMap.put("BTCUSDT", priceData);

        when(userRepository.findByEmail("test@example.com")).thenReturn(Optional.of(user));
        when(itemRepository.findByUserIdWithTransactions(1L)).thenReturn(List.of(item));
        when(priceService.getBatchPrices("BTCUSDT")).thenReturn(priceMap);

        List<ItemDTO> result = itemService.getItemsByUserId(1L, "test@example.com");

        assertEquals(1, result.size());
        assertEquals("BTCUSDT", result.get(0).getName());
        assertEquals(60000.0, result.get(0).getCurrentPrice());
    }

    @Test
    void testGetItemsByUserId_ShouldReturnEmpty_WhenNoItems() {
        User user = new User();
        user.setId(1L);

        when(userRepository.findByEmail("test@example.com")).thenReturn(Optional.of(user));
        when(itemRepository.findByUserIdWithTransactions(1L)).thenReturn(List.of());

        List<ItemDTO> result = itemService.getItemsByUserId(1L, "test@example.com");

        assertTrue(result.isEmpty());
    }

    @Test
    void testGetItemsByUserId_ShouldThrowAccessDenied_WhenCallerIsNotOwner() {
        User caller = new User();
        caller.setId(2L); // different from requested userId 1L

        when(userRepository.findByEmail("other@example.com")).thenReturn(Optional.of(caller));

        assertThrows(AccessDeniedException.class, () ->
                itemService.getItemsByUserId(1L, "other@example.com"));
    }

    @Test
    void testDeleteItemById_ShouldReturnTrue_WhenItemExistsAndOwned() {
        User user = new User();
        user.setId(1L);

        Item item = new Item();
        item.setId(5L);
        item.setUser(user);

        when(itemRepository.findById(5L)).thenReturn(Optional.of(item));
        when(userRepository.findByEmail("test@example.com")).thenReturn(Optional.of(user));

        boolean result = itemService.deleteItemById(5L, "test@example.com");

        assertTrue(result);
        verify(itemRepository).delete(item);
    }

    @Test
    void testDeleteItemById_ShouldReturnFalse_WhenItemNotFound() {
        when(itemRepository.findById(99L)).thenReturn(Optional.empty());

        boolean result = itemService.deleteItemById(99L, "test@example.com");

        assertFalse(result);
        verify(itemRepository, never()).delete(any());
    }

    @Test
    void testDeleteItemById_ShouldThrowAccessDenied_WhenCallerIsNotOwner() {
        User itemOwner = new User();
        itemOwner.setId(1L);
        User caller = new User();
        caller.setId(2L);

        Item item = new Item();
        item.setId(5L);
        item.setUser(itemOwner);

        when(itemRepository.findById(5L)).thenReturn(Optional.of(item));
        when(userRepository.findByEmail("other@example.com")).thenReturn(Optional.of(caller));

        assertThrows(AccessDeniedException.class, () ->
                itemService.deleteItemById(5L, "other@example.com"));

        verify(itemRepository, never()).delete(any());
    }
}
