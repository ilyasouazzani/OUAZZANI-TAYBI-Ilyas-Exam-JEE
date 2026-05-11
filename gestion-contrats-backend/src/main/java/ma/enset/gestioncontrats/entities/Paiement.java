package ma.enset.gestioncontrats.entities;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;

/**
 * Entité Paiement - représente un versement effectué sur un contrat.
 * Relation N..1 avec Contrat : plusieurs paiements peuvent correspondre au même contrat.
 */
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Paiement {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDate datePaiement;
    private Double montant;
    private String modePaiement;  // VIREMENT, CARTE, CHEQUE, ESPECES

    /**
     * Paiement est le côté "propriétaire" (porte la FK contrat_id).
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "contrat_id", nullable = false)
    private Contrat contrat;
}
