package com.company.util;

import com.company.dto.JwtDTO;
import com.company.enums.GeneralRole;
import com.company.exception.NotPermissionException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import java.util.Date;

public class JwtUtil {
    private static final String secretKey = "Sekratniy_kalit";

    public static String encode(String id, GeneralRole role) {
        JwtBuilder jwtBuilder = Jwts.builder();
        jwtBuilder.setIssuedAt(new Date()); // 18:58:00
        jwtBuilder.setExpiration(new Date(System.currentTimeMillis() + (60 * 60 * 60 * 1000))); // 19:58:00
        jwtBuilder.setIssuer("Mazgi production");
        jwtBuilder.signWith(SignatureAlgorithm.HS256, secretKey);
        jwtBuilder.claim("id", id);
        jwtBuilder.claim("role", role.name());

        return jwtBuilder.compact();
    }

    public static String decode(String token) {
        Claims claims = Jwts.parser()
                .setSigningKey(secretKey)
                .parseClaimsJws(token)
                .getBody();
        return (String) claims.get("id");
    }

    public static Integer decode(String token, GeneralRole requiredRole) {
        Claims claims = Jwts.parser()
                .setSigningKey(secretKey)
                .parseClaimsJws(token)
                .getBody();
        Integer id = (Integer) claims.get("id");
        String role = (String) claims.get("role");

        if (!requiredRole.equals(GeneralRole.valueOf(role))) {
            throw new NotPermissionException("Not Access");
        }
        return id;
    }

    public static JwtDTO decodeJwtDTO(String token) {
        Claims claims = Jwts.parser()
                .setSigningKey(secretKey)
                .parseClaimsJws(token)
                .getBody();
        Integer id = (Integer) claims.get("id");
        String role = (String) claims.get("role");

        return new JwtDTO(id, GeneralRole.valueOf(role));
    }
}
