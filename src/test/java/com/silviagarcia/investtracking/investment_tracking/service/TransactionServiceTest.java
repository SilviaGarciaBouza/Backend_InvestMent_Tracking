package com.silviagarcia.investtracking.investment_tracking.service;

import com.silviagarcia.investtracking.investment_tracking.dto.TransactionDTO;
import com.silviagarcia.investtracking.investment_tracking.model.Item;
import com.silviagarcia.investtracking.investment_tracking.model.Transaction;
import com.silviagarcia.investtracking.investment_tracking.repository.ItemRepository;
import com.silviagarcia.investtracking.investment_tracking.repository.TransactionRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TransactionServiceTest {

    @Mock private TransactionRepository transactionRepository;
    @Mock private ItemRepository itemRepository;
    @InjectMocks private TransactionService transactionService;

    @Test
    void testCreateTransaction_ShouldCalculateTotal() {
        Long itemId = 1L;
        Item item = new Item();
        item.setId(itemId);

        TransactionDTO inputDto = new TransactionDTO();
        inputDto.setStocks(10.0);
        inputDto.setPurchasePrice(5.0);

        when(itemRepository.findById(itemId)).thenReturn(Optional.of(item));
        when(transactionRepository.save(any(Transaction.class))).thenAnswer(i -> i.getArguments()[0]);

        TransactionDTO result = transactionService.createTransaction(inputDto, itemId);

        assertEquals(50.0, result.getInvEur(), "Deberia ser 10 * 5 = 50");
        verify(transactionRepository).save(any(Transaction.class));
    }
}
