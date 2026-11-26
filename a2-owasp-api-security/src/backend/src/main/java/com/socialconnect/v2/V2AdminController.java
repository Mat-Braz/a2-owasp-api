package com.socialconnect.v2;

import com.socialconnect.repository.UserRepository;
import com.socialconnect.security.CurrentUser;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/api/v2/admin")
@Tag(name = "v2 - segura")
public class V2AdminController {

    private final UserRepository userRepository;
    private final CurrentUser currentUser;

    public V2AdminController(UserRepository userRepository, CurrentUser currentUser) {
        this.userRepository = userRepository;
        this.currentUser = currentUser;
    }

    @DeleteMapping("/users/{id}")
    @Operation(summary = "So ADMIN apaga usuarios.")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id, HttpServletRequest request) {
        if (!"ADMIN".equals(currentUser.require(request).role())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Apenas administradores podem apagar usuários");
        }

        if (!userRepository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuário não encontrado");
        }

        userRepository.deleteById(id);

        return ResponseEntity.noContent().build();
    }
}
