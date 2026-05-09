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
class ItemRepositoryTest {

    @Autowired
    private TestEntityManager em;

    @Autowired
    private ItemRepository itemRepository;

    private User persistUser() {
        User user = new User();
        user.setUsername("testuser");
        user.setPassword("hashed");
        user.setEmail("test@repo.com");
        return em.persistAndFlush(user);
    }

    private Category persistCategory() {
        Category cat = new Category();
        cat.setName("Crypto");
        return em.persistAndFlush(cat);
    }

    private Item persistItem(User user, Category cat, String name) {
        Item item = new Item();
        item.setName(name);
        item.setUser(user);
        item.setCategory(cat);
        return em.persistAndFlush(item);
    }

    @Test
    void findByUserIdWithTransactions_ShouldReturnItemsWithTransactions() {
        User user = persistUser();
        Category cat = persistCategory();
        Item item = persistItem(user, cat, "BTCUSDT");

        Transaction tx = new Transaction();
        tx.setStocks(1.0);
        tx.setPurchasePrice(60000.0);
        tx.setInvEur(60000.0);
        tx.setPurchaseDate(LocalDateTime.now());
        tx.setItem(item);
        em.persistAndFlush(tx);

        em.clear();

        List<Item> result = itemRepository.findByUserIdWithTransactions(user.getId());

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getName()).isEqualTo("BTCUSDT");
        assertThat(result.get(0).getTransactions()).hasSize(1);
    }

    @Test
    void findByUserIdWithTransactions_ShouldReturnItemWithNoTransactions() {
        User user = persistUser();
        Category cat = persistCategory();
        persistItem(user, cat, "AAPL");

        em.clear();

        List<Item> result = itemRepository.findByUserIdWithTransactions(user.getId());

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getTransactions()).isEmpty();
    }

    @Test
    void findByUserIdWithTransactions_ShouldReturnEmpty_WhenNoItems() {
        List<Item> result = itemRepository.findByUserIdWithTransactions(999L);

        assertThat(result).isEmpty();
    }
}
