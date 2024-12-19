package com.example.sponsors;

import com.example.sponsors.model.User;
import com.example.sponsors.repository.UserRepository;
import com.example.sponsors.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserServiceTest {

	private static final Logger logger = LoggerFactory.getLogger(UserServiceTest.class);

	@InjectMocks
	private UserService userService;

	@Mock
	private UserRepository userRepository;

	@Mock
	private PasswordEncoder passwordEncoder;

	@BeforeEach
	void setUp() {
		MockitoAnnotations.openMocks(this);
		assertNotNull(passwordEncoder, "PasswordEncoder não foi inicializado corretamente.");
		logger.info("Mocks foram inicializados.");
	}

	@Test
	void testCreateUser() {
		User user = new User();
		user.setName("Test User");
		user.setEmail("test@example.com");
		user.setPassword("password123");

		logger.info("Iniciando teste de criação de usuário com dados: {}", user);

		when(userRepository.findByEmail(user.getEmail())).thenReturn(Optional.empty());
		logger.info("Nenhum usuário encontrado com o e-mail: {}", user.getEmail());

		when(passwordEncoder.encode(user.getPassword())).thenReturn("encodedPassword");
		logger.info("Senha do usuário codificada.");

		when(userRepository.save(any(User.class))).thenReturn(user);
		logger.info("Usuário salvo no mock repository: {}", user);

		User createdUser = userService.createUser(user);

		logger.info("Usuário criado: {}", createdUser);

		assertNotNull(createdUser);
		assertEquals("Test User", createdUser.getName());
		verify(userRepository, times(1)).save(user);
	}

	@Test
	void testFindAllUsers() {
		List<User> userList = List.of(new User(), new User());
		when(userRepository.findAll()).thenReturn(userList);

		logger.info("Iniciando teste para buscar todos os usuários.");

		List<User> result = userService.findAllUsers();

		logger.info("Usuários encontrados: {}", result);

		assertNotNull(result);
		assertEquals(2, result.size());
		verify(userRepository, times(1)).findAll();
	}

	@Test
	void testFindUserById() {
		User user = new User();
		user.setId(1L);
		user.setName("Test User");
		when(userRepository.findById(1L)).thenReturn(Optional.of(user));

		logger.info("Iniciando teste para buscar usuário por ID: 1");

		Optional<User> result = userService.findUserById(1L);

		logger.info("Usuário encontrado: {}", result.orElse(null));

		assertTrue(result.isPresent());
		assertEquals("Test User", result.get().getName());
		verify(userRepository, times(1)).findById(1L);
	}

	@Test
	void testUpdateUser() {
		User existingUser = new User();
		existingUser.setId(1L);
		existingUser.setName("Old Name");
		existingUser.setEmail("old@example.com");

		User userDetails = new User();
		userDetails.setName("New Name");
		userDetails.setEmail("new@example.com");

		when(userRepository.findById(1L)).thenReturn(Optional.of(existingUser));
		logger.info("Usuário encontrado para atualização: {}", existingUser);

		when(userRepository.save(any(User.class))).thenReturn(existingUser);

		User updatedUser = userService.updateUser(1L, userDetails);

		logger.info("Usuário atualizado: {}", updatedUser);

		assertNotNull(updatedUser);
		assertEquals("New Name", updatedUser.getName());
		assertEquals("new@example.com", updatedUser.getEmail());
		verify(userRepository, times(1)).save(existingUser);
	}

	@Test
	void testDeleteUser() {
		User user = new User();
		user.setId(1L);

		when(userRepository.findById(1L)).thenReturn(Optional.of(user));
		logger.info("Usuário encontrado para exclusão: {}", user);

		doNothing().when(userRepository).delete(user);

		userService.deleteUser(1L);

		logger.info("Usuário excluído com sucesso.");

		verify(userRepository, times(1)).delete(user);
	}
}
