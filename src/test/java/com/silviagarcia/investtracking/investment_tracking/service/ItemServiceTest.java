package com.silviagarcia.investtracking.investment_tracking.service;


import com.silviagarcia.investtracking.investment_tracking.model.Category;
import com.silviagarcia.investtracking.investment_tracking.model.Item;
import com.silviagarcia.investtracking.investment_tracking.model.Transaction;
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
    void testSaveItemFromMap_ShouldCreateItemAndInitialTransaction() {
        Map<String, Object> data = Map.of(
                "name", "Bitcoin",
                "userId", 1,
                "categoryId", 1,
                "initialStocks", 0.5,
                "initialPrice", 40000.0
        );

        User user = new User();
        Category cat = new Category();

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(categoryRepository.findById(1L)).thenReturn(Optional.of(cat));
        when(itemRepository.save(any(Item.class))).thenAnswer(i -> {
            Item it = (Item) i.getArguments()[0];
            it.setId(100L);
            return it;
        });

        var result = itemService.saveItemFromMap(data);

        assertNotNull(result);
        assertEquals("Bitcoin", result.getName());
        verify(itemRepository).save(any(Item.class));
        verify(transactionRepository).save(any(Transaction.class));
    }
}
