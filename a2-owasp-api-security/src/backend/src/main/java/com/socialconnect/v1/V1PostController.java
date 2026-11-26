package com.socialconnect.v1;

import com.socialconnect.domain.Post;
import com.socialconnect.repository.PostRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.List;

@RestController
@RequestMapping("/api/v1/posts")
@Tag(name = "v1 - vulneravel")
public class V1PostController {

    private final PostRepository postRepository;
    private final JdbcTemplate jdbcTemplate;

    public V1PostController(PostRepository postRepository, JdbcTemplate jdbcTemplate) {
        this.postRepository = postRepository;
        this.jdbcTemplate = jdbcTemplate;
    }

    @GetMapping
    @Operation(summary = "Feed publico. Nao e a falha.")
    public List<Post> feed() {
        return postRepository.findByHiddenFalse();
    }

    @GetMapping("/search")
    @Operation(summary = "Busca vulneravel a SQL injection por concatenacao.")
    public List<Post> search(@RequestParam("q") String q) {
        String sql = "SELECT * FROM posts WHERE content LIKE '%" + q + "%' AND is_private = false";

        return jdbcTemplate.query(sql, (rs, rowNum) -> mapPost(rs));
    }

    @GetMapping("/{id}")
    @Operation(summary = "BOLA: devolve qualquer post pelo ID, inclusive privado.")
    public Post get(@PathVariable Long id) {
        return postRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Post não encontrado"));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "BOLA: apaga qualquer post pelo ID.")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Post não encontrado"));

        postRepository.delete(post);

        return ResponseEntity.noContent().build();
    }

    private Post mapPost(ResultSet rs) throws SQLException {
        Post post = new Post();
        post.setId(asLong(column(rs, "id")));
        post.setContent(rs.getString(indexOf(rs, "content")));
        post.setPrivate(asBoolean(column(rs, "is_private")));
        post.setAuthorId(asLong(column(rs, "author_id")));

        return post;
    }

    private Object column(ResultSet rs, String name) throws SQLException {
        return rs.getObject(indexOf(rs, name));
    }

    private int indexOf(ResultSet rs, String name) throws SQLException {
        ResultSetMetaData meta = rs.getMetaData();
        for (int i = 1; i <= meta.getColumnCount(); i++) {
            String label = meta.getColumnLabel(i);
            if (name.equalsIgnoreCase(label) || name.equalsIgnoreCase(meta.getColumnName(i))) {
                return i;
            }
        }
        throw new SQLException("Coluna ausente no resultado: " + name);
    }

    private Long asLong(Object value) {
        if (value instanceof Number number) {
            return number.longValue();
        }
        return Long.valueOf(String.valueOf(value));
    }

    private boolean asBoolean(Object value) {
        if (value instanceof Boolean bool) {
            return bool;
        }
        if (value instanceof Number number) {
            return number.intValue() != 0;
        }
        return Boolean.parseBoolean(String.valueOf(value));
    }
}
