package test;

import org.azkiTest.config.JwtTokenUtil;
import org.junit.jupiter.api.Test;
import org.springframework.security.jwt.Jwt;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class TokenTest {
    JwtTokenUtil jwtTokenUtil = new JwtTokenUtil();

        @Test
        public void testIsTokenExpired() {
            String token = jwtTokenUtil.generateToken("username");
            assertFalse(jwtTokenUtil.isTokenExpired(token));
        }

        @Test
        public void testValidateToken_ValidToken() {
            String token = jwtTokenUtil.generateToken("username");
            assertTrue(jwtTokenUtil.validateToken(token));
        }

        @Test
        public void testValidateToken_InvalidToken() {
            String validToken = jwtTokenUtil.generateToken("username");
            String invalidToken = validToken + "modified";
            assertFalse(jwtTokenUtil.validateToken(invalidToken));
        }

    }


