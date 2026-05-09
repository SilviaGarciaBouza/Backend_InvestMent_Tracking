package com.silviagarcia.investtracking.investment_tracking.service;

import com.silviagarcia.investtracking.investment_tracking.dto.UserDTO;
import com.silviagarcia.investtracking.investment_tracking.model.User;
import com.silviagarcia.investtracking.investment_tracking.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock private UserRepository userRepository;
    @Mock private BCryptPasswordEncoder passwordEncoder;

    @InjectMocks private UserService userService;

    @Test
    void testRegisterUser_ShouldEncodePassword() {
        User user = new User();
        user.setPassword("12345");
        user.setUsername("silvia");

        when(passwordEncoder.encode("12345")).thenReturn("encoded_12345");
        when(userRepository.save(any(User.class))).thenReturn(user);

        UserDTO result = userService.registerUser(user);

        verify(passwordEncoder).encode("12345");
        assertEquals("encoded_12345", user.getPassword());
        assertNotNull(result);
    }

    @Test
    void testFindEntityByEmail_ShouldReturnUser_WhenExists() {
        User user = new User();
        user.setEmail("silvia@test.com");
        when(userRepository.findByEmail("silvia@test.com")).thenReturn(java.util.Optional.of(user));

        User result = userService.findEntityByEmail("silvia@test.com");

        assertNotNull(result);
        assertEquals("silvia@test.com", result.getEmail());
    }

    @Test
    void testFindEntityByEmail_ShouldReturnNull_WhenNotFound() {
        when(userRepository.findByEmail("unknown@test.com")).thenReturn(java.util.Optional.empty());

        User result = userService.findEntityByEmail("unknown@test.com");

        assertNull(result);
    }

    @Test
    void testFindEntityByUsername_ShouldReturnUser_WhenExists() {
        User user = new User();
        user.setUsername("silvia");
        when(userRepository.findByUsername("silvia")).thenReturn(java.util.Optional.of(user));

        User result = userService.findEntityByUsername("silvia");

        assertNotNull(result);
        assertEquals("silvia", result.getUsername());
    }
}
