package com.example.sponsors.controller;

import com.example.sponsors.model.User;
import com.example.sponsors.service.JwtService;
import com.example.sponsors.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private JwtService jwtService;

    @Autowired
    private UserService userService;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @GetMapping
    public ResponseEntity<List<User>> getAllUsers() {
        return ResponseEntity.ok(userService.findAllUsers());
    }

    @GetMapping("/{id}")
    public ResponseEntity<User> getUserById(@PathVariable Long id) {
        return userService.findUserById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/send-validation-token")
    public ResponseEntity<String> sendValidationToken(@RequestParam String password) {
        String token = jwtService.generateToken(password);
        return ResponseEntity.ok("Token gerado com sucesso.");
    }

    @PostMapping("/register")
    public ResponseEntity<String> registerUser(@RequestParam String token, @Valid @RequestBody User user) {
        try {
            String passwordHash = jwtService.validateToken(token);
            if (!passwordEncoder.matches(user.getPassword(), passwordHash)) {
                return ResponseEntity.badRequest().body("Senha inválida.");
            }
            userService.createUser(user);
            return ResponseEntity.ok("Usuário cadastrado com sucesso.");
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<User> updateUser(@PathVariable Long id, @Valid @RequestBody User userDetails) {
        try {
            User updatedUser = userService.updateUser(id, userDetails);
            return ResponseEntity.ok(updatedUser);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody User loginRequest) {
        try {
            return userService.findByEmail(loginRequest.getEmail())
                    .map(user -> {
                        if (passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())) {
                            String token = jwtService.generateToken(user.getEmail());
                            return ResponseEntity.ok(Map.of("token", "Bearer " + token));
                        } else {
                            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Credenciais inválidas");
                        }
                    })
                    .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).body("Usuário não encontrado"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro no processo de login: " + e.getMessage());
        }
    }

    @PutMapping("/{id}/update-password")
    public ResponseEntity<String> updatePassword(@PathVariable Long id, @RequestBody Map<String, String> payload) {
        try {
            String currentPassword = payload.get("currentPassword");
            String newPassword = payload.get("newPassword");
            userService.updatePassword(id, currentPassword, newPassword);
            return ResponseEntity.ok("Senha atualizada com sucesso.");
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
