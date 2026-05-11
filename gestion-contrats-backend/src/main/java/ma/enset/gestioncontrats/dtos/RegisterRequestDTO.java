package ma.enset.gestioncontrats.dtos;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class RegisterRequestDTO {
    @NotBlank
    private String username;

    @NotBlank
    @Size(min = 6, message = "Le mot de passe doit faire au moins 6 caractères")
    private String password;

    @Email
    private String email;

    // Rôle souhaité : CLIENT, EMPLOYE, ADMIN (en prod, on ne laisserait pas choisir librement)
    private String role;
}
