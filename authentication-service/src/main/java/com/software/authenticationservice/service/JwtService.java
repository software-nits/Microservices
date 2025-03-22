package com.software.authenticationservice.service;

import com.software.authenticationservice.dto.ValidateTokenResponse;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Service
public class JwtService {
    private final static String SECRET_KEY ="9Pfzo6NMzqsRtIG4JItkV9QN998XOUE7O7Q36vlW0xSwnQZGXUDtISDRe2KvbjDt";

    @Autowired
    private UserDetailsService userDetailsService;

    public String generateToken(UserDetails userDetails){
        return generateToken(new HashMap<>(), userDetails);
    }
    public String extractEmail(String token) {
        return extractClaim(token, Claims::getSubject);
    }
    public Boolean isTokenValid(String token, UserDetails userDetails) {
        final String userName = extractEmail(token);
        return userName.equals(userDetails.getUsername()) && isTokenExpired(token);
    }

    private String generateToken(Map<String, Object> extraClaims, UserDetails userDetails){
        return Jwts.builder().setClaims(extraClaims).setSubject(userDetails.getUsername())
                .setIssuedAt(new Date(System.currentTimeMillis())).setExpiration(new Date(System.currentTimeMillis()+24*60*60*1000))
                .signWith(getSignInKey(), SignatureAlgorithm.HS256).compact();
    }
    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date(System.currentTimeMillis()+24*60*60*1000));
    }

    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    private <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        return claimsResolver.apply(extractAllClaims(token));
    }

    private Claims extractAllClaims(String token){
        return Jwts.parserBuilder().setSigningKey(getSignInKey()).build().parseClaimsJws(token).getBody();
    }

    private Key getSignInKey() {
        return Keys.hmacShaKeyFor(Decoders.BASE64.decode(SECRET_KEY));
    }

    public ValidateTokenResponse validateToken(String token) {
        String username = extractEmail(token.replace("Bearer ", ""));
        UserDetails userDetails = userDetailsService.loadUserByUsername(username);
        return new ValidateTokenResponse(isTokenValid(token.replace("Bearer ", ""), userDetails));

    }
}
