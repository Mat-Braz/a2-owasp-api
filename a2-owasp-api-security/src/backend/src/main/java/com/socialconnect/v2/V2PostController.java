package com.socialconnect.v2;

import com.socialconnect.domain.Post;
import com.socialconnect.repository.PostRepository;
import com.socialconnect.security.AuthUser;
import com.socialconnect.security.CurrentUser;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/api/v2/posts")
@Tag(name = "v2 - segura")
public class V2PostController {

    private final PostRepository postRepository;
    private final CurrentUser currentUser;

    public V2PostController(PostRepository postRepository, CurrentUser currentUser) {
        this.postRepository = postRepository;
        this.currentUser = currentUser;
    }

    @GetMapping
    @Operation(summary = "Feed publico.")
    public List<Post> feed() {
        return postRepository.findByHiddenFalse();
    }

    @GetMapping("/search")
    @Operation(summary = "Busca parametrizada. O payload de SQLi vira texto literal.")
    public List<Post> search(@RequestParam("q") String q) {
        return postRepository.searchPublic(q);
    }

    @GetMapping("/{id}")
    @Operation(summary = "BOLA corrigido: post privado so para o autor.")
    public Post get(@PathVariable Long id, HttpServletRequest request) {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Post não encontrado"));

        AuthUser logged = currentUser.require(request);
        Long loggedUserId = logged.id();

        if (!post.isPrivate() || post.getAuthorId().equals(loggedUserId)) {
            return post;
        }

        throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Você não pode acessar este post");
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "So o autor apaga o post.")
    public ResponseEntity<Void> delete(@PathVariable Long id, HttpServletRequest request) {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Post não encontrado"));

        AuthUser logged = currentUser.require(request);

        if (!post.getAuthorId().equals(logged.id())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Você não pode apagar este post");
        }

        postRepository.delete(post);

        return ResponseEntity.noContent().build();
    }
}
