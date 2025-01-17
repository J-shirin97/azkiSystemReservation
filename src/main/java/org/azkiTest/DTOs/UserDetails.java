package org.azkiTest.DTOs;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.azkiTest.model.Users;

@Getter
@Setter
@AllArgsConstructor
public class UserDetails {
    private String username;
    private String password;



    public static Users toEntity(UserDetails dto) {
        Users user = new Users();
        user.setUsername(dto.getUsername());
        user.setPassword(dto.getPassword());
        return user;
    }
}
