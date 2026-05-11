package ma.enset.gestioncontrats.dtos;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * DTO reçu du frontend pour créer ou modifier un client.
 * Les annotations @NotBlank/@Email servent à la validation automatique
 * (@Valid dans le Controller déclenchera les erreurs si les règles ne sont pas respectées).
 */
@Data
public class ClientRequestDTO {

    @NotBlank(message = "Le nom est obligatoire")
    private String nom;

    @NotBlank(message = "Le prénom est obligatoire")
    private String prenom;

    @NotBlank(message = "L'email est obligatoire")
    @Email(message = "Format d'email invalide")
    private String email;

    private String telephone;
}
