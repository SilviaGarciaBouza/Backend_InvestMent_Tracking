package com.silviagarcia.investtracking.investment_tracking.service;

import com.silviagarcia.investtracking.investment_tracking.dto.TransactionDTO;
import com.silviagarcia.investtracking.investment_tracking.model.Item;
import com.silviagarcia.investtracking.investment_tracking.model.Transaction;
import com.silviagarcia.investtracking.investment_tracking.model.User;
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

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TransactionServiceTest {

    @Mock private TransactionRepository transactionRepository;
    @Mock private ItemRepository itemRepository;
    @Mock private UserRepository userRepository;
    @InjectMocks private TransactionService transactionService;

    // --- createTransaction ---

    @Test
    void testCreateTransaction_ShouldAutoCalculateInvEur_WhenNotProvided() {
        Long itemId = 1L;
        User user = new User();
        user.setId(5L);
        Item item = new Item();
        item.setId(itemId);
        item.setUser(user);

        TransactionDTO dto = new TransactionDTO();
        dto.setStocks(10.0);
        dto.setPurchasePrice(5.0);

        when(itemRepository.findById(itemId)).thenReturn(Optional.of(item));
        when(transactionRepository.save(any(Transaction.class))).thenAnswer(i -> i.getArguments()[0]);
        when(userRepository.findByEmail("test@example.com")).thenReturn(Optional.of(user));

        TransactionDTO result = transactionService.createTransaction(dto, itemId, "test@example.com");

        assertEquals(50.0, result.getInvEur(), "Deberia ser 10 * 5 = 50");
        verify(transactionRepository).save(any(Transaction.class));
    }

    @Test
    void testCreateTransaction_ShouldUseExplicitInvEur_WhenProvided() {
        Long itemId = 1L;
        User user = new User();
        user.setId(5L);
        Item item = new Item();
        item.setId(itemId);
        item.setUser(user);

        TransactionDTO dto = new TransactionDTO();
        dto.setStocks(1.0);
        dto.setPurchasePrice(100.0);
        dto.setInvEur(200.0); // explicitly provided, not 1.0 * 100.0

        when(itemRepository.findById(itemId)).thenReturn(Optional.of(item));
        when(transactionRepository.save(any())).thenAnswer(i -> i.getArguments()[0]);
        when(userRepository.findByEmail("test@example.com")).thenReturn(Optional.of(user));

        TransactionDTO result = transactionService.createTransaction(dto, itemId, "test@example.com");

        assertEquals(200.0, result.getInvEur());
    }

    @Test
    void testCreateTransaction_ShouldAutoSetPurchaseDate_WhenNull() {
        Long itemId = 1L;
        User user = new User();
        user.setId(5L);
        Item item = new Item();
        item.setId(itemId);
        item.setUser(user);

        TransactionDTO dto = new TransactionDTO();
        dto.setStocks(1.0);
        dto.setPurchasePrice(50.0);
        // purchaseDate not set

        when(itemRepository.findById(itemId)).thenReturn(Optional.of(item));
        when(transactionRepository.save(any())).thenAnswer(i -> i.getArguments()[0]);
        when(userRepository.findByEmail("test@example.com")).thenReturn(Optional.of(user));

        TransactionDTO result = transactionService.createTransaction(dto, itemId, "test@example.com");

        assertNotNull(result.getPurchaseDate());
    }

    @Test
    void testCreateTransaction_ShouldThrow400_WhenStocksIsNull() {
        TransactionDTO dto = new TransactionDTO();
        dto.setStocks(null);
        dto.setPurchasePrice(100.0);

        assertThrows(ResponseStatusException.class, () ->
                transactionService.createTransaction(dto, 1L, "test@example.com"));

        verifyNoInteractions(itemRepository, transactionRepository);
    }

    @Test
    void testCreateTransaction_ShouldThrow400_WhenPurchasePriceIsNull() {
        TransactionDTO dto = new TransactionDTO();
        dto.setStocks(1.0);
        dto.setPurchasePrice(null);

        assertThrows(ResponseStatusException.class, () ->
                transactionService.createTransaction(dto, 1L, "test@example.com"));

        verifyNoInteractions(itemRepository, transactionRepository);
    }

    @Test
    void testCreateTransaction_ShouldThrow404_WhenItemNotFound() {
        TransactionDTO dto = new TransactionDTO();
        dto.setStocks(1.0);
        dto.setPurchasePrice(100.0);

        when(itemRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(ResponseStatusException.class, () ->
                transactionService.createTransaction(dto, 99L, "test@example.com"));
    }

    @Test
    void testCreateTransaction_ShouldThrowAccessDenied_WhenCallerIsNotOwner() {
        User itemOwner = new User();
        itemOwner.setId(1L);
        User caller = new User();
        caller.setId(2L);

        Item item = new Item();
        item.setId(1L);
        item.setUser(itemOwner);

        TransactionDTO dto = new TransactionDTO();
        dto.setStocks(1.0);
        dto.setPurchasePrice(100.0);

        when(itemRepository.findById(1L)).thenReturn(Optional.of(item));
        when(userRepository.findByEmail("other@example.com")).thenReturn(Optional.of(caller));

        assertThrows(AccessDeniedException.class, () ->
                transactionService.createTransaction(dto, 1L, "other@example.com"));

        verify(transactionRepository, never()).save(any());
    }

    // --- deleteTransaction ---

    @Test
    void testDeleteTransaction_ShouldReturnTrue_WhenExistsAndOwned() {
        User user = new User();
        user.setId(1L);

        Item item = new Item();
        item.setUser(user);

        Transaction tx = new Transaction();
        tx.setId(10L);
        tx.setItem(item);

        when(transactionRepository.findById(10L)).thenReturn(Optional.of(tx));
        when(userRepository.findByEmail("test@example.com")).thenReturn(Optional.of(user));

        boolean result = transactionService.deleteTransaction(10L, "test@example.com");

        assertTrue(result);
        verify(transactionRepository).delete(tx);
    }

    @Test
    void testDeleteTransaction_ShouldReturnFalse_WhenNotFound() {
        when(transactionRepository.findById(99L)).thenReturn(Optional.empty());

        boolean result = transactionService.deleteTransaction(99L, "test@example.com");

        assertFalse(result);
        verify(transactionRepository, never()).delete(any());
    }

    @Test
    void testDeleteTransaction_ShouldThrowAccessDenied_WhenCallerIsNotOwner() {
        User itemOwner = new User();
        itemOwner.setId(1L);
        User caller = new User();
        caller.setId(2L);

        Item item = new Item();
        item.setUser(itemOwner);

        Transaction tx = new Transaction();
        tx.setId(10L);
        tx.setItem(item);

        when(transactionRepository.findById(10L)).thenReturn(Optional.of(tx));
        when(userRepository.findByEmail("other@example.com")).thenReturn(Optional.of(caller));

        assertThrows(AccessDeniedException.class, () ->
                transactionService.deleteTransaction(10L, "other@example.com"));

        verify(transactionRepository, never()).delete(any());
    }
}
