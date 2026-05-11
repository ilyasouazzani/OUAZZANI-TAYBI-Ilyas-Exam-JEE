package ma.enset.gestioncontrats.controllers;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import org.springframework.context.annotation.Configuration;

/**
 * Configuration Swagger / OpenAPI 3.
 *
 * @OpenAPIDefinition : métadonnées visibles en haut de la page Swagger UI.
 * @SecurityScheme : déclare le schéma d'auth JWT (Bearer token) pour
 * que Swagger affiche le bouton "Authorize" — utile à l'étape 5.
 *
 * URL : http://localhost:8085/swagger-ui.html
 */
@Configuration
@OpenAPIDefinition(
    info = @Info(
        title = "API Gestion des Contrats d'Assurance",
        version = "1.0",
        description = "Projet Exam JEE - ENSET Mohammedia - Filière BDCC"
    )
)
@SecurityScheme(
    name = "bearerAuth",
    type = SecuritySchemeType.HTTP,
    scheme = "bearer",
    bearerFormat = "JWT"
)
public class SwaggerConfig {
    // SpringDoc lit les annotations et génère la doc automatiquement
    // Rien à coder ici, la config est dans les annotations
}
