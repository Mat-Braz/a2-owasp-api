package com.socialconnect.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "posts")
@Getter
@Setter
@NoArgsConstructor
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 2000)
    private String content;

    @Getter(AccessLevel.NONE)
    @Setter(AccessLevel.NONE)
    @Column(name = "is_private", nullable = false)
    private boolean hidden;

    @Column(name = "author_id", nullable = false)
    private Long authorId;

    @JsonProperty("isPrivate")
    public boolean isPrivate() {
        return hidden;
    }

    @JsonProperty("isPrivate")
    public void setPrivate(boolean isPrivate) {
        this.hidden = isPrivate;
    }
}
