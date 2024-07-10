package com.example.DigitalWallet.Jwt;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

import org.springframework.stereotype.Component;

@Component
public class JwtService {
    
    private  final static String SECRET="5367566B59703373367639792F423F4528482B4D6251655468576D5A71347437";

    public String generateToken(String username){
        Map<String,Object>claims=new HashMap<>();
        return createToken(claims,username);
    }


    private String createToken(Map<String, Object> claims, String username) {
        return Jwts.builder()
        .setClaims(claims)
        .setSubject(username)
       .setIssuedAt(new java.util.Date(System.currentTimeMillis()))
       .setExpiration(new Date(System.currentTimeMillis()+1000*60*30))
        .signWith(getSignKey(),SignatureAlgorithm.HS256)
        .compact();
    }

    private Key getSignKey() {
       byte[] keyBytes = Decoders.BASE64.decode(SECRET);
       return Keys.hmacShaKeyFor(keyBytes);
    }

    public String extractUsername(String token){
        return extractClaim(token,Claims::getSubject);
    }

    public Date extractExpiraton(String token){
        return (Date) extractClaim(token, Claims::getExpiration);
    }

    public <T>T extractClaim(String token,Function<Claims,T> claimresolver){
        final Claims claim=extractAllClaims(token);
        return claimresolver.apply(claim);
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parserBuilder().setSigningKey(getSignKey()).build()
        .parseClaimsJws(token).getBody();
    }

    private boolean isTokenExpired(String token){
        return extractExpiraton(token).before(new Date());
    
    }

    public boolean isTokenValid(String token){
        return !isTokenExpired(token);
    
    }
}
