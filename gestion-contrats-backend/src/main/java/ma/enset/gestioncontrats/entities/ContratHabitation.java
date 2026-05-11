package ma.enset.gestioncontrats.entities;

import jakarta.persistence.*;
import lombok.*;

/**
 * Contrat d'assurance habitation.
 * Champs spécifiques : type de logement, adresse, superficie.
 */
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@PrimaryKeyJoinColumn(name = "id")
public class ContratHabitation extends Contrat {

    private String type;        // appartement, villa, studio, etc.
    private String adresse;
    private Double superficie;  // en m²
}
