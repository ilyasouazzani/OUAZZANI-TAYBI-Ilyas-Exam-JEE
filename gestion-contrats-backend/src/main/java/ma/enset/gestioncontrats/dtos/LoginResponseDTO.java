package ma.enset.gestioncontrats.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Set;

/**
 * Réponse après un login réussi.
 * Le frontend Angular va stocker ce token et l'envoyer
 * dans le header Authorization de chaque requête suivante.
 */
@Data
@AllArgsConstructor
public class LoginResponseDTO {
    private String token;
    private String username;
    private Set<String> roles;
}
