package com.example.sponsors.controller;

import com.example.sponsors.dto.RegisterRequestDTO;
import com.example.sponsors.model.User;
import com.example.sponsors.repository.UserRepository;
import com.example.sponsors.service.JwtService;
import com.example.sponsors.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;


import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @GetMapping
    public ResponseEntity<String> getUser() {
        return ResponseEntity.ok("sucesso!");
    }

    @GetMapping("/all")
    public ResponseEntity<List<User>> getAllUsers() {
        return ResponseEntity.ok(userService.findAllUsers());
    }

    @GetMapping("/{id}")
    public ResponseEntity<User> getUserById(@PathVariable Long id) {
        return userService.findUserById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<User> updateUser(@PathVariable Long id, @RequestBody RegisterRequestDTO body) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));
        if (body.name() != null && !body.name().isEmpty()) {
            user.setName(body.name());
        }
        if (body.email() != null && !body.email().isEmpty()) {
            user.setEmail(body.email());
        }
        if (body.password() != null && !body.password().isEmpty()) {
            user.setPassword(passwordEncoder.encode(body.password()));
        } // Atualiza a senha criptografada

        userRepository.save(user);

        User updatedUser = new User(user.getId(), user.getName(), user.getEmail());
        return ResponseEntity.ok(updatedUser);
    }
        @DeleteMapping("/{id}")
        public ResponseEntity<User> deleteUser (@PathVariable Long id){
            userService.deleteUser(id);
            return ResponseEntity.noContent().build();
        }
}
