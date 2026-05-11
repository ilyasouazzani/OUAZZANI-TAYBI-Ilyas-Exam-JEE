package ma.enset.gestioncontrats.entities;

import jakarta.persistence.*;
import lombok.*;

/**
 * Contrat d'assurance automobile.
 *
 * @PrimaryKeyJoinColumn : indique à JPA que la PK de cette table (id)
 * est aussi une FK qui référence la table CONTRAT.
 * C'est ce qui "joint" les deux tables dans la stratégie JOINED.
 */
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@PrimaryKeyJoinColumn(name = "id")
public class ContratAuto extends Contrat {

    private String immatriculation;
    private String marque;
    private String modele;
}
