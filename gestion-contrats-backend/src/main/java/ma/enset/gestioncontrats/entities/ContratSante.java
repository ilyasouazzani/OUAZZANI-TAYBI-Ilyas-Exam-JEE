package ma.enset.gestioncontrats.entities;

import jakarta.persistence.*;
import lombok.*;

/**
 * Contrat d'assurance santé.
 * Champs spécifiques : niveau de couverture, nombre de personnes couvertes.
 */
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@PrimaryKeyJoinColumn(name = "id")
public class ContratSante extends Contrat {

    private String niveauCouverture;  // BASIQUE, STANDARD, PREMIUM
    private Integer nbPersonnes;
}
