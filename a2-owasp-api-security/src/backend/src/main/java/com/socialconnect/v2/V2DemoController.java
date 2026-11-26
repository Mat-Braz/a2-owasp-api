package com.socialconnect.v2;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v2/demo")
@Tag(name = "v2 - segura")
public class V2DemoController {

    @GetMapping("/crash")
    @Operation(summary = "Dispara um erro 500 com mensagem generica, sem stack.")
    public void crash() {
        throw new IllegalStateException("Falha interna de exemplo: conexão com serviço de billing");
    }
}
