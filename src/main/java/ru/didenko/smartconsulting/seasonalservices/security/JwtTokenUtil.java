package ru.didenko.smartconsulting.seasonalservices.security;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import ru.didenko.smartconsulting.seasonalservices.service.userDetails.CustomUserDetails;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

/**
 * Class with utilities to work with token:
 * <ul>
 * <li>Generate token;</li>
 * <li>Set claims about authenticated user;</li>
 * <li>Get username from token.</li>
 * </ul>
 */
@Component
public class JwtTokenUtil {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    @Value("${spring.security.jwt.validity}")
    private long JWT_TOKEN_VALIDITY;

    @Value("${spring.security.jwt.secret}")
    private String secret;

    public String generateToken(CustomUserDetails userDetails) {
        return Jwts.builder()
                .setClaims(setTokenData(userDetails))
                .setSubject(userDetails.toString())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + JWT_TOKEN_VALIDITY))
                .signWith(SignatureAlgorithm.HS256, secret)
                .compact();
    }

    private Map<String, Object> setTokenData(CustomUserDetails userDetails) {
        Map<String, Object> tokenData = new HashMap<>();
        tokenData.put("user_id", userDetails.getUserId());
        tokenData.put("username", userDetails.getUsername());
        tokenData.put("user_role", userDetails.getAuthorities().toString());
        return tokenData;
    }

    public String getUsernameFromToken(String token) {
        String subject = getClaimsFromToken(token, Claims::getSubject);
        JsonNode subjectJson = null;
        try {
            subjectJson = objectMapper.readTree(subject);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        if (subjectJson != null) {
            return subjectJson.get("username").asText();
        } else {
            return null;
        }
    }

    private <T> T getClaimsFromToken(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = getAllClaimsFromToken(token);
        return claimsResolver.apply(claims);
    }

    private Claims getAllClaimsFromToken(String token) {
        return Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody();
    }
}
