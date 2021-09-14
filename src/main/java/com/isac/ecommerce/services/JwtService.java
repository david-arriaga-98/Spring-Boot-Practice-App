package com.isac.ecommerce.services;

import com.isac.ecommerce.config.UserDetailsImpl;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class JwtService {

    private static final Logger logger = LoggerFactory.getLogger(JwtService.class);

    private final static String ISSUER = "fluxcommerce.com";
    private final static int EXPIRES_IN = 1000 * 60 * 60;
    private final static String SECRET_KEY = "ebe52e54a693e005c981b8d37b6dd353";

    public String generateJwtToken(Authentication authentication) {
        UserDetailsImpl userPrincipal = (UserDetailsImpl) authentication.getPrincipal();
        return Jwts.builder()
                .setHeaderParam("typ", "JWT")
                .setSubject(userPrincipal.getUsername())
                .setIssuer(ISSUER)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRES_IN))
                .signWith(SignatureAlgorithm.HS256, SECRET_KEY)
                .compact();

    }

    public String getEmailFromJwtToken(String authToken) {
        return Jwts.parser().requireIssuer(ISSUER).setSigningKey(SECRET_KEY).parseClaimsJws(authToken).getBody().getSubject();
    }

    public boolean validateJwtToken(String authToken) {
        try {
            Jwts.parser().requireIssuer(ISSUER).setSigningKey(SECRET_KEY).parseClaimsJws(authToken);
            return true;
        } catch (Exception ex) {
            logger.error("Invalid Jwt Token, {}", ex.getMessage());
        }
        return false;
    }

}
