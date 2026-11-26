package com.socialconnect.v2;

import com.socialconnect.domain.User;
import com.socialconnect.dto.UserResponseDTO;
import com.socialconnect.dto.UserUpdateDTO;
import com.socialconnect.repository.UserRepository;
import com.socialconnect.security.CurrentUser;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/api/v2/users")
@Tag(name = "v2 - segura")
public class V2UserController {

    private final UserRepository userRepository;
    private final CurrentUser currentUser;

    public V2UserController(UserRepository userRepository, CurrentUser currentUser) {
        this.userRepository = userRepository;
        this.currentUser = currentUser;
    }

    @GetMapping("/{id}")
    @Operation(summary = "DTO sem senha.")
    public UserResponseDTO get(@PathVariable Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuário não encontrado"));

        return UserResponseDTO.from(user);
    }

    @PatchMapping("/{id}")
    @Operation(summary = "So a bio do proprio usuario. role no JSON e ignorado.")
    public UserResponseDTO update(@PathVariable Long id, @RequestBody UserUpdateDTO body, HttpServletRequest request) {
        if (!currentUser.require(request).id().equals(id)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Você só pode editar o próprio perfil");
        }

        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuário não encontrado"));

        if (body.getBio() != null) {
            user.setBio(body.getBio());
        }

        return UserResponseDTO.from(userRepository.save(user));
    }
}
