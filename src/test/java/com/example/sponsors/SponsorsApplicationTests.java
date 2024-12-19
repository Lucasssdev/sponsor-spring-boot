package com.example.sponsors;

import com.example.sponsors.model.User;
import com.example.sponsors.repository.UserRepository;
import com.example.sponsors.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.junit.jupiter.api.Assertions;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
class SponsorsApplicationTests {

	@Mock
	private UserRepository userRepository;


	@InjectMocks
	private UserService userService;

	@Autowired
	private PasswordEncoder passwordEncoder;


	@Test
	public void testPasswordEncoding() {
		// Senha em texto plano
		String rawPassword = "senha1234";

		// Gerando o hash da senha
		String hashedPassword = passwordEncoder.encode(rawPassword);

		// Exibindo o hash no console
		System.out.println("Senha codificada: " + hashedPassword);

		// Verificando se o hash não é nulo
		assertNotNull(hashedPassword);
	}


	@Test
	void createUserDeveLancarExcecaoSeSenhaInvalida() {
		User user = new User();
		user.setEmail("test@example.com");
		user.setPassword(null);

		IllegalArgumentException exception = assertThrows(
				IllegalArgumentException.class,
				() -> userService.createUser(user),
				"Senha não pode ser nula ou vazia"
		);

		assertEquals("Senha não pode ser nula ou vazia", exception.getMessage());
	}


	@Test
	void createUser_ThrowsException_WhenEmailExists() {
		User newUser = new User();
		newUser.setEmail("test@example.com");
		newUser.setPassword("password123");

		when(userRepository.findByEmail("test@example.com")).thenReturn(Optional.of(new User()));

		RuntimeException exception = assertThrows(RuntimeException.class, () -> {
			userService.createUser(newUser);
		});

		assertEquals("Já existe um usuário com este email", exception.getMessage());
	}


	@Test
	void updateUser_ThrowsException_WhenUserNotFound() {
		Long userId = 1L;
		User userDetails = new User();

		when(userRepository.findById(userId)).thenReturn(Optional.empty());

		RuntimeException exception = assertThrows(RuntimeException.class, () -> {
			userService.updateUser(userId, userDetails);
		});

		assertEquals("Usuário não encontrado com ID: 1", exception.getMessage());
	}

	@Test
	void deleteUser_Success() {
		Long userId = 1L;
		User existingUser = new User();
		existingUser.setId(userId);

		when(userRepository.findById(userId)).thenReturn(Optional.of(existingUser));

		userService.deleteUser(userId);

		verify(userRepository).findById(userId);
		verify(userRepository).delete(existingUser);
	}

	@Test
	void deleteUser_ThrowsException_WhenUserNotFound() {
		Long userId = 1L;

		when(userRepository.findById(userId)).thenReturn(Optional.empty());

		RuntimeException exception = assertThrows(RuntimeException.class, () -> {
			userService.deleteUser(userId);
		});

		assertEquals("Usuário não encontrado", exception.getMessage());
	}

	@Test
	public void findUserById_ReturnsEmptyOptional_WhenIdDoesNotExist() {
		// Mock dos dados
		when(userRepository.findById(1L)).thenReturn(Optional.empty());

		// Chamada do método
		Optional<User> result = userService.findUserById(1L);

		// Verificações
		assertFalse(result.isPresent());

		// Verificação do mock
		verify(userRepository, times(1)).findById(1L);
	}
	@Test
	public void findUserById_ReturnsUser_WhenIdExists() {
		// Mock dos dados
		User user = new User("nome", "email@example.com", "senha");
		when(userRepository.findById(1L)).thenReturn(Optional.of(user));

		// Chamada do método
		Optional<User> result = userService.findUserById(1L);

		// Verificações
		assertTrue(result.isPresent());
		assertEquals("nome", result.get().getName());
		assertEquals("email@example.com", result.get().getEmail());

		// Verificação do mock
		verify(userRepository, times(1)).findById(1L);
	}

}
