package com.socialconnect.v2;

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
@RequestMapping("/api/v2")
@Tag(name = "v2 - segura")
public class V2AuthController {

    private final AuthService authService;

    public V2AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    @Operation(summary = "Login. O filtro da v2 valida assinatura HMAC e expiracao.")
    public LoginResponse login(@RequestBody LoginRequest request) {
        return authService.login(request);
    }
}
