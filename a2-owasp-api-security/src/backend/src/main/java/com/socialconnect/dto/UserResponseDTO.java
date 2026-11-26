package com.socialconnect.dto;

import com.socialconnect.domain.User;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UserResponseDTO {

    private Long id;
    private String username;
    private String role;
    private String bio;

    public static UserResponseDTO from(User user) {
        return new UserResponseDTO(user.getId(), user.getUsername(), user.getRole(), user.getBio());
    }
}
