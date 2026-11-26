package com.socialconnect.v1;

import com.socialconnect.domain.User;
import com.socialconnect.repository.UserRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/api/v1/users")
@Tag(name = "v1 - vulneravel")
public class V1UserController {

    private final UserRepository userRepository;

    public V1UserController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @GetMapping("/{id}")
    @Operation(summary = "Devolve a entidade User inteira, inclusive senha e role.")
    public User get(@PathVariable Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuário não encontrado"));
    }

    @PatchMapping("/{id}")
    @Operation(summary = "Mass assignment: aceita a entidade User e grava role se vier no JSON.")
    public User update(@PathVariable Long id, @RequestBody User body) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuário não encontrado"));

        if (body.getBio() != null) {
            user.setBio(body.getBio());
        }

        if (body.getRole() != null) {
            user.setRole(body.getRole());
        }

        if (body.getPassword() != null) {
            user.setPassword(body.getPassword());
        }

        if (body.getUsername() != null) {
            user.setUsername(body.getUsername());
        }

        return userRepository.save(user);
    }
}
