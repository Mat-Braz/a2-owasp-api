package com.socialconnect.v1;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/demo")
@Tag(name = "v1 - vulneravel")
public class V1DemoController {

    @GetMapping("/crash")
    @Operation(summary = "Dispara um erro 500 com stack trace na resposta.")
    public void crash() {
        throw new IllegalStateException("Falha interna de exemplo: conexão com serviço de billing");
    }
}
