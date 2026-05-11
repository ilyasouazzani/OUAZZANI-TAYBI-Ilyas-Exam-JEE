package ma.enset.gestioncontrats.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

/**
 * DTO pour créer/modifier un contrat auto.
 * Contient les champs communs du contrat + les champs spécifiques auto.
 */
@Data
public class ContratAutoRequestDTO {

    @NotNull(message = "L'identifiant du client est obligatoire")
    private Long clientId;

    @NotNull(message = "Le montant est obligatoire")
    @Positive(message = "Le montant doit être positif")
    private Double montant;

    @NotNull(message = "La durée est obligatoire")
    @Positive
    private Integer duree;

    @NotNull
    @Positive
    private Double taux;

    // Champs spécifiques ContratAuto
    @NotBlank(message = "L'immatriculation est obligatoire")
    private String immatriculation;

    @NotBlank(message = "La marque est obligatoire")
    private String marque;

    @NotBlank(message = "Le modèle est obligatoire")
    private String modele;
}
