package com.silviagarcia.investtracking.investment_tracking.repository;

import com.silviagarcia.investtracking.investment_tracking.model.Category;
import com.silviagarcia.investtracking.investment_tracking.model.Item;
import com.silviagarcia.investtracking.investment_tracking.model.Transaction;
import com.silviagarcia.investtracking.investment_tracking.model.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
class TransactionRepositoryTest {

    @Autowired
    private TestEntityManager em;

    @Autowired
    private TransactionRepository transactionRepository;

    private Item persistItem() {
        User user = new User();
        user.setUsername("testuser");
        user.setPassword("hashed");
        user.setEmail("tx-repo@test.com");
        em.persistAndFlush(user);

        Category cat = new Category();
        cat.setName("Crypto");
        em.persistAndFlush(cat);

        Item item = new Item();
        item.setName("BTCUSDT");
        item.setUser(user);
        item.setCategory(cat);
        return em.persistAndFlush(item);
    }

    private Transaction persistTransaction(Item item, double stocks, double price) {
        Transaction tx = new Transaction();
        tx.setStocks(stocks);
        tx.setPurchasePrice(price);
        tx.setInvEur(stocks * price);
        tx.setPurchaseDate(LocalDateTime.now());
        tx.setItem(item);
        return em.persistAndFlush(tx);
    }

    @Test
    void findByItemId_ShouldReturnAllTransactionsForItem() {
        Item item = persistItem();
        persistTransaction(item, 1.0, 60000.0);
        persistTransaction(item, 0.5, 62000.0);

        List<Transaction> result = transactionRepository.findByItemId(item.getId());

        assertThat(result).hasSize(2);
        assertThat(result).extracting(Transaction::getStocks).containsExactlyInAnyOrder(1.0, 0.5);
    }

    @Test
    void findByItemId_ShouldReturnEmpty_WhenNoTransactions() {
        Item item = persistItem();

        List<Transaction> result = transactionRepository.findByItemId(item.getId());

        assertThat(result).isEmpty();
    }
}
