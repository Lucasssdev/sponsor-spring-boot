package com.example.sponsors.controller;

import com.example.sponsors.model.User;
import com.example.sponsors.service.JwtService;
import com.example.sponsors.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private JwtService jwtService;

    @GetMapping
    public ResponseEntity<List<User>> getAllUsers() {
        return ResponseEntity.ok(userService.findAllUsers());
    }

    @GetMapping("/{id}")
    public ResponseEntity<User> getUserById(@PathVariable Long id) {
        return ResponseEntity.of(userService.findUserById(id));
    }

    @PostMapping("/send-validation-token")
    public ResponseEntity<String> sendValidationToken(@RequestParam String password) {
        userService.validateAndGenerateToken(password);
        return ResponseEntity.ok("Token gerado com sucesso.");
    }

    @PostMapping("/register")
    public ResponseEntity<String> registerUser(@RequestBody User user) {
        userService.createUser(user);
        return ResponseEntity.ok("Usuário cadastrado com sucesso.");
    }

    @PutMapping("/{id}")
    public ResponseEntity<User> updateUser(@PathVariable Long id,  @RequestBody User userDetails) {
        return ResponseEntity.ok(userService.updateUser(id, userDetails));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody User loginRequest) {
        return ResponseEntity.ok(userService.authenticateUser(loginRequest));
    }

    @PutMapping("/{id}/update-password")
    public ResponseEntity<String> updatePassword(@PathVariable Long id, @RequestBody Map<String, String> payload) {
        userService.updatePassword(id, payload.get("currentPassword"), payload.get("newPassword"));
        return ResponseEntity.ok("Senha atualizada com sucesso.");
    }
}
