package com.socialconnect.v1;

import com.socialconnect.dto.LoginRequest;
import com.socialconnect.dto.LoginResponse;
import com.socialconnect.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1")
@Tag(name = "v1 - vulneravel")
public class V1AuthController {

    private final AuthService authService;

    public V1AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    @Operation(summary = "Login. O token emitido e valido; a falha API2 esta no filtro da v1.")
    public LoginResponse login(@RequestBody LoginRequest request) {
        return authService.login(request);
    }
}
