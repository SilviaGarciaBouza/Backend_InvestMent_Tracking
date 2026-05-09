package com.silviagarcia.investtracking.investment_tracking.repository;

import com.silviagarcia.investtracking.investment_tracking.model.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
class UserRepositoryTest {

    @Autowired
    private TestEntityManager em;

    @Autowired
    private UserRepository userRepository;

    private User persistUser(String username, String email) {
        User user = new User();
        user.setUsername(username);
        user.setPassword("hashed");
        user.setEmail(email);
        return em.persistAndFlush(user);
    }

    @Test
    void findByEmail_ShouldReturnUser_WhenExists() {
        persistUser("silvia", "silvia@test.com");

        Optional<User> result = userRepository.findByEmail("silvia@test.com");

        assertThat(result).isPresent();
        assertThat(result.get().getEmail()).isEqualTo("silvia@test.com");
    }

    @Test
    void findByEmail_ShouldReturnEmpty_WhenNotFound() {
        Optional<User> result = userRepository.findByEmail("noexiste@test.com");

        assertThat(result).isEmpty();
    }

    @Test
    void findByUsername_ShouldReturnUser_WhenExists() {
        persistUser("alberto", "alberto@test.com");

        Optional<User> result = userRepository.findByUsername("alberto");

        assertThat(result).isPresent();
        assertThat(result.get().getUsername()).isEqualTo("alberto");
    }

    @Test
    void findByUsername_ShouldReturnEmpty_WhenNotFound() {
        Optional<User> result = userRepository.findByUsername("unknown");

        assertThat(result).isEmpty();
    }
}
