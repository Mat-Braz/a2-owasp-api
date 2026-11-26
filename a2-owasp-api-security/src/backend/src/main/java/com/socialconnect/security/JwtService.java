package com.socialconnect.security;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.socialconnect.domain.User;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.time.Instant;
import java.util.Base64;
import java.util.LinkedHashMap;
import java.util.Map;

@Service
public class JwtService {

    private final String secret;
    private final long ttlSeconds;
    private final ObjectMapper objectMapper;

    public JwtService(
            @Value("${socialconnect.jwt.secret}") String secret,
            @Value("${socialconnect.jwt.ttl-seconds}") long ttlSeconds,
            ObjectMapper objectMapper) {
        this.secret = secret;
        this.ttlSeconds = ttlSeconds;
        this.objectMapper = objectMapper;
    }

    public String issue(User user) {
        try {
            String header = encode("{\"alg\":\"HS256\",\"typ\":\"JWT\"}");

            Map<String, Object> claims = new LinkedHashMap<>();
            claims.put("sub", String.valueOf(user.getId()));
            claims.put("username", user.getUsername());
            claims.put("role", user.getRole());
            claims.put("exp", Instant.now().getEpochSecond() + ttlSeconds);

            String payload = encode(objectMapper.writeValueAsString(claims));
            String unsigned = header + "." + payload;

            return unsigned + "." + sign(unsigned);
        } catch (ResponseStatusException ex) {
            throw ex;
        } catch (Exception ex) {
            throw new IllegalStateException("Falha ao emitir o token", ex);
        }
    }

    public AuthUser readPayloadOnly(String token) {
        String[] parts = token.split("\\.");
        if (parts.length < 2 || parts[1].isBlank()) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Token malformado");
        }
        return toUser(decode(parts[1]));
    }

    public AuthUser parseAndVerify(String token) {
        String[] parts = token.split("\\.");

        if (parts.length != 3) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Token malformado");
        }

        String unsigned = parts[0] + "." + parts[1];

        if (!same(sign(unsigned), parts[2])) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Assinatura invalida");
        }

        AuthUser user = toUser(decode(parts[1]));

        if (user.exp() <= Instant.now().getEpochSecond()) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Token expirado");
        }

        return user;
    }

    private AuthUser toUser(String json) {
        try {
            JsonNode node = objectMapper.readTree(json);
            if (node == null || !node.hasNonNull("sub")) {
                throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Token sem sujeito");
            }
            long id = Long.parseLong(node.get("sub").asText());
            String username = node.path("username").asText("");
            String role = node.path("role").asText("");
            long exp = node.path("exp").asLong(0);

            return new AuthUser(id, username, role, exp);
        } catch (ResponseStatusException ex) {
            throw ex;
        } catch (Exception ex) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Token malformado");
        }
    }

    private String sign(String unsigned) {
        try {
            Mac mac = Mac.getInstance("HmacSHA256");
            mac.init(new SecretKeySpec(secret.getBytes(StandardCharsets.UTF_8), "HmacSHA256"));
            return encode(mac.doFinal(unsigned.getBytes(StandardCharsets.UTF_8)));
        } catch (Exception ex) {
            throw new IllegalStateException("Falha ao assinar o token", ex);
        }
    }

    private String encode(String value) {
        return encode(value.getBytes(StandardCharsets.UTF_8));
    }

    private String encode(byte[] value) {
        return Base64.getUrlEncoder().withoutPadding().encodeToString(value);
    }

    private String decode(String segment) {
        try {
            int remainder = segment.length() % 4;
            String padded = remainder == 0 ? segment : segment + "=".repeat(4 - remainder);
            byte[] bytes = Base64.getUrlDecoder().decode(padded);

            return new String(bytes, StandardCharsets.UTF_8);
        } catch (IllegalArgumentException ex) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Token malformado");
        }
    }

    private boolean same(String left, String right) {
        return MessageDigest.isEqual(
                left.getBytes(StandardCharsets.UTF_8),
                right.getBytes(StandardCharsets.UTF_8));
    }
}
