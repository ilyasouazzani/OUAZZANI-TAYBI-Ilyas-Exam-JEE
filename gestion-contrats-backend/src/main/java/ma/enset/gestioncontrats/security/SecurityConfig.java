package ma.enset.gestioncontrats.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;

/**
 * Configuration Spring Security TEMPORAIRE (Étape 1).
 *
 * But : permettre le démarrage et le test sans être bloqué par la sécurité.
 * Cette classe sera ENTIÈREMENT remplacée à l'étape 5 avec JWT + rôles.
 *
 * On désactive le CSRF (inutile pour une API REST stateless)
 * et on autorise toutes les requêtes sans authentification.
 */
@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .csrf(AbstractHttpConfigurer::disable)
            .headers(headers -> headers
                // Nécessaire pour que la console H2 s'affiche (elle utilise des iframes)
                .frameOptions(frame -> frame.sameOrigin())
            )
            .authorizeHttpRequests(auth -> auth
                .anyRequest().permitAll()
            );
        return http.build();
    }
}
