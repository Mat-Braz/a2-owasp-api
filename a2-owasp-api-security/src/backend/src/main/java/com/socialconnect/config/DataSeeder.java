package com.socialconnect.config;

import com.socialconnect.domain.Post;
import com.socialconnect.domain.User;
import com.socialconnect.repository.PostRepository;
import com.socialconnect.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DataSeeder implements CommandLineRunner {

    private static final Logger log = LoggerFactory.getLogger(DataSeeder.class);

    private final UserRepository userRepository;
    private final PostRepository postRepository;

    public DataSeeder(UserRepository userRepository, PostRepository postRepository) {
        this.userRepository = userRepository;
        this.postRepository = postRepository;
    }

    @Override
    public void run(String... args) {
        if (userRepository.count() > 0) {
            return;
        }

        User alice = saveUser("alice", "123456", "USER", "Olá, eu sou a Alice.");
        User bob = saveUser("bob", "123456", "USER", "Olá, eu sou o Bob.");
        User admin = saveUser("admin", "admin123", "ADMIN", "Conta administradora da SocialConnect.");

        Post welcome = savePost("Bem-vindos ao meu perfil na SocialConnect!", false, alice.getId());
        Post draft = savePost("Rascunho confidencial da Alice", true, alice.getId());

        if (!Long.valueOf(1L).equals(alice.getId())
                || !Long.valueOf(2L).equals(bob.getId())
                || !Long.valueOf(3L).equals(admin.getId())
                || !Long.valueOf(1L).equals(welcome.getId())
                || !Long.valueOf(2L).equals(draft.getId())) {
            throw new IllegalStateException("O seed não gerou os IDs esperados (users 1-3, posts 1-2)");
        }

        log.info("SocialConnect pronta. Contas: alice, bob (senha 123456) e admin (senha admin123).");
    }

    private User saveUser(String username, String password, String role, String bio) {
        User user = new User();

        user.setUsername(username);
        user.setPassword(password);
        user.setRole(role);
        user.setBio(bio);

        return userRepository.saveAndFlush(user);
    }

    private Post savePost(String content, boolean isPrivate, Long authorId) {
        Post post = new Post();

        post.setContent(content);
        post.setPrivate(isPrivate);
        post.setAuthorId(authorId);

        return postRepository.saveAndFlush(post);
    }
}
