package org.azkiTest.DTOs;

import lombok.Getter;
import lombok.Setter;
import org.azkiTest.model.Users;

@Getter
@Setter
public class LoginRequest {
    private String username;
    private String password;

    public LoginRequest(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public Users mapToEntity() {
        Users user = new Users();
        user.setUsername(this.username);
        user.setPassword(this.password);
        return user;
    }
}
