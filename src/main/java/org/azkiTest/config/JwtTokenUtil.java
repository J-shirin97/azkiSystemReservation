package org.azkiTest.config;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.jwt.Jwt;
import org.springframework.security.jwt.JwtHelper;
import org.springframework.security.jwt.crypto.sign.MacSigner;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
public class JwtTokenUtil {

    @Value("${security.jwt.expiration-time}")
    private Long expirationTime;  // Remove static keyword

    private static final String SECRET_KEY = "a7123fe89249b2be2f4ebbc8206d4630b3245c122785b304bfed3b6a375d67e7";

    public String generateToken(String username) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("sub", username);
        claims.put("iat", new Date().getTime());
        claims.put("exp", new Date().getTime() + expirationTime);

        return JwtHelper.encode(mapToJson(claims), new MacSigner(SECRET_KEY)).getEncoded();
    }

    private String mapToJson(Map<String, Object> claims) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            return objectMapper.writeValueAsString(claims);
        } catch (IOException e) {
            throw new RuntimeException("Error serializing claims to JSON", e);
        }
    }

    public Jwt parseToken(String token) {
        try {
            return JwtHelper.decodeAndVerify(token, new MacSigner(SECRET_KEY));
        } catch (Exception e) {
            throw new RuntimeException("Invalid JWT token", e);
        }
    }

    public boolean isTokenExpired(String token) {
        Jwt jwt = parseToken(token);
        String claims = jwt.getClaims();
        long expirationTimestamp = extractExpirationFromClaims(claims);
        Date expirationDate = new Date(expirationTimestamp * 1000);
        return expirationDate.before(new Date());
    }


    public boolean validateToken(String token) {
        try {
            parseToken(token);
            return !isTokenExpired(token);
        } catch (RuntimeException e) {
            return false;
        }
    }

    public long extractExpirationFromClaims(String claims) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode claimsNode = objectMapper.readTree(claims);
            JsonNode expNode = claimsNode.get("exp");
            if (expNode != null) {
                return expNode.asLong();
            } else {
                throw new RuntimeException("Expiration claim ('exp') not found in JWT.");
            }
        } catch (IOException e) {
            throw new RuntimeException("Error parsing JWT claims", e);
        }
    }

    public String extractUsername(String token) {
        Jwt jwt = parseToken(token);
        String claims = jwt.getClaims();
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode claimsNode = objectMapper.readTree(claims);
            JsonNode usernameNode = claimsNode.get("sub");
            if (usernameNode != null) {
                return usernameNode.asText();
            } else {
                throw new RuntimeException("Username claim ('sub') not found in JWT.");
            }
        } catch (IOException e) {
            throw new RuntimeException("Error extracting username from JWT claims", e);
        }
    }
    public Authentication getAuthentication(String token, UserDetails userDetails) {
        return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
    }

}
