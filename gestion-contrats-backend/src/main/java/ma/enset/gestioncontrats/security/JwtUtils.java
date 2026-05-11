package ma.enset.gestioncontrats.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;

/**
 * Utilitaire JWT : génération, validation et extraction du username.
 *
 * On utilise HMAC-SHA256 (HS256) pour signer les tokens.
 * La clé secrète doit faire au moins 256 bits — on la génère depuis une string.
 *
 * Durée de validité : 24h (modifiable via JWT_EXPIRATION_MS).
 */
@Component
public class JwtUtils {

    // En prod, cette clé doit être dans application.properties ou une variable d'env
    private static final String JWT_SECRET = "enset-mohammedia-bdcc-exam-jee-secret-key-2024-très-longue";
    private static final long JWT_EXPIRATION_MS = 86400000L; // 24 heures

    // Génère la clé de signature à partir du secret
    private SecretKey getSigningKey() {
        return Keys.hmacShaKeyFor(JWT_SECRET.getBytes());
    }

    /**
     * Génère un token JWT pour un utilisateur authentifié.
     * Le token contient : le username (subject), la date d'émission, la date d'expiration.
     */
    public String generateToken(UserDetails userDetails) {
        return Jwts.builder()
                .subject(userDetails.getUsername())
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + JWT_EXPIRATION_MS))
                .signWith(getSigningKey())
                .compact();
    }

    /**
     * Extrait le username (subject) depuis un token JWT.
     */
    public String extractUsername(String token) {
        return Jwts.parser()
                .verifyWith(getSigningKey())
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .getSubject();
    }

    /**
     * Valide un token : vérifie la signature ET que le token n'est pas expiré.
     */
    public boolean validateToken(String token, UserDetails userDetails) {
        try {
            String username = extractUsername(token);
            return username.equals(userDetails.getUsername()) && !isTokenExpired(token);
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }

    private boolean isTokenExpired(String token) {
        Date expiration = Jwts.parser()
                .verifyWith(getSigningKey())
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .getExpiration();
        return expiration.before(new Date());
    }
}
