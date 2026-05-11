package ma.enset.gestioncontrats.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

@Data
public class ContratSanteRequestDTO {

    @NotNull
    private Long clientId;

    @NotNull @Positive
    private Double montant;

    @NotNull @Positive
    private Integer duree;

    @NotNull @Positive
    private Double taux;

    // Champs spécifiques ContratSante
    @NotBlank(message = "Le niveau de couverture est obligatoire")
    private String niveauCouverture; // BASIQUE, STANDARD, PREMIUM

    @NotNull @Positive
    private Integer nbPersonnes;
}
