package com.socialconnect.repository;

import com.socialconnect.domain.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PostRepository extends JpaRepository<Post, Long> {

    List<Post> findByHiddenFalse();

    @Query("SELECT p FROM Post p WHERE LOWER(p.content) LIKE LOWER(CONCAT('%', :q, '%')) AND p.hidden = false")
    List<Post> searchPublic(@Param("q") String q);
}
