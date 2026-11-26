package com.socialconnect.v1;

import com.socialconnect.repository.UserRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/api/v1/admin")
@Tag(name = "v1 - vulneravel")
public class V1AdminController {

    private final UserRepository userRepository;

    public V1AdminController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @DeleteMapping("/users/{id}")
    @Operation(summary = "BFLA: qualquer usuario logado apaga uma conta.")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        if (!userRepository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuário não encontrado");
        }

        userRepository.deleteById(id);

        return ResponseEntity.noContent().build();
    }
}
