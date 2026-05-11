package ma.enset.gestioncontrats.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

@Data
public class ContratHabitationRequestDTO {

    @NotNull
    private Long clientId;

    @NotNull @Positive
    private Double montant;

    @NotNull @Positive
    private Integer duree;

    @NotNull @Positive
    private Double taux;

    // Champs spécifiques ContratHabitation
    @NotBlank(message = "Le type de logement est obligatoire")
    private String type;

    @NotBlank(message = "L'adresse est obligatoire")
    private String adresse;

    @NotNull @Positive
    private Double superficie;
}
