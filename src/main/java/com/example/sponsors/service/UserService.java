package com.example.sponsors.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.example.sponsors.model.User;
import com.example.sponsors.repository.UserRepository;

import jakarta.transaction.Transactional;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class UserService {

    private static final Logger log = LoggerFactory.getLogger(UserService.class);

    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final JwtService jwtService;

    @Autowired
    public UserService(PasswordEncoder passwordEncoder, UserRepository userRepository, JwtService jwtService) {
        this.passwordEncoder = passwordEncoder;
        this.userRepository = userRepository;
        this.jwtService = jwtService;
    }

    public List<User> findAllUsers() {
        log.info("Buscando todos os usuários");
        return userRepository.findAll();
    }

    public Optional<User> findUserById(Long id) {
        log.info("Buscando usuário com ID: {}", id);
        return userRepository.findById(id);
    }

    @Transactional
    public User createUser(User user) {
        log.info("Criando novo usuário");

        if (user.getPassword() == null || user.getPassword().isEmpty()) {
            throw new IllegalArgumentException("Senha não pode ser nula ou vazia");
        }

        userRepository.findByEmail(user.getEmail()).ifPresent(existingUser -> {
            throw new RuntimeException("Já existe um usuário com este email");
        });

        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }

    public User updateUser(Long id, User userDetails) {
        log.info("Atualizando usuário com ID: {}", id);

        // Buscar o usuário existente no banco
        User existingUser = userRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("Usuário não encontrado com ID: {}", id);
                    return new RuntimeException("Usuário não encontrado com ID: " + id);
                });

        // Atualizar apenas os campos não nulos
        if (userDetails.getName() != null && !userDetails.getName().isEmpty()) {
            existingUser.setName(userDetails.getName());
        }

        if (userDetails.getEmail() != null && !userDetails.getEmail().isEmpty()) {
            existingUser.setEmail(userDetails.getEmail());
        }

        // Verifique se a senha foi atualizada (se necessário)
        if (userDetails.getPassword() != null && !userDetails.getPassword().isEmpty()) {
            existingUser.setPassword(passwordEncoder.encode(userDetails.getPassword()));
        }

        // Salvar alterações
        User updatedUser = userRepository.save(existingUser);
        log.info("Usuário atualizado com sucesso: {}", updatedUser.getId());
        return updatedUser;
    }


    public void deleteUser(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        userRepository.delete(user);
    }

    public Map<String, String> authenticateUser(User loginRequest) {
        User user = userRepository.findByEmail(loginRequest.getEmail())
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        if (!passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())) {
            throw new RuntimeException("Credenciais inválidas");
        }

        String token = jwtService.generateToken(user.getEmail());
        return Map.of("token", "Bearer " + token);
    }

    @Transactional
    public void updatePassword(Long id, String currentPassword, String newPassword) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        if (!passwordEncoder.matches(currentPassword, user.getPassword())) {
            throw new RuntimeException("Senha atual incorreta");
        }

        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
    }

    public void validateAndGenerateToken(String password) {
        if (password == null || password.isEmpty()) {
            throw new RuntimeException("Senha não pode ser nula ou vazia");
        }

        jwtService.generateToken(password);
    }
}
