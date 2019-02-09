package com.atkison.login.seed.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import com.atkison.login.seed.model.User;

import org.springframework.stereotype.Component;

@Component
public class JwtGenerator {
    public String generate(User user)
    {
        Claims claims = Jwts.claims()
                .setSubject(user.getEmail());
        claims.put("userId", String.valueOf(user.getId()));
        claims.put("email", user.getEmail());
        claims.put("firstname", user.getFirstname());
        claims.put("lastname", user.getLastname());

        return Jwts.builder()
                .setClaims(claims)
                .signWith(SignatureAlgorithm.HS512, "prayerrush")
                .compact();
    }
}
