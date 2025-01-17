package org.azkiTest.DTOs;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;


@Setter
@Getter
@AllArgsConstructor
public class TokenResponse {
    private String token;
    private String expiration;

    public TokenResponse() {

    }
}
