package org.azkiTest.DTOs;


import lombok.Getter;
import lombok.Setter;
import org.azkiTest.model.Users;

@Getter
@Setter
public class RegisterRequest {
    private String username;
    private String email;
    private String password;

    public RegisterRequest(String username, String email, String password) {
        this.username = username;
        this.email = email;
        this.password = password;
    }

    public Users mapToEntity() {
        Users user = new Users();
        user.setUsername(this.username);
        user.setEmail(this.email);
        user.setPassword(this.password);
        return user;
    }
}
