package ma.enset.gestioncontrats.entities;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;
import java.util.List;

/**
 * Classe parente abstraite pour tous les types de contrats.
 *
 * Stratégie d'héritage choisie : JOINED
 * - Table CONTRAT : stocke les champs communs (id, montant, statut, etc.)
 * - Tables CONTRAT_AUTO, CONTRAT_HABITATION, CONTRAT_SANTE : stockent les champs spécifiques
 * - Chaque table enfant partage la PK de la table parente (FK + PK en même temps)
 *
 * Avantage : schéma normalisé, pas de colonnes NULL inutiles.
 */
@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@Data
@NoArgsConstructor
@AllArgsConstructor
public abstract class Contrat {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDate dateSouscription;

    /**
     * @Enumerated(STRING) : stocke "VALIDE" en BDD plutôt que l'index 2.
     * Plus lisible et résistant aux refactorings de l'enum.
     */
    @Enumerated(EnumType.STRING)
    private StatutContrat statut;

    private LocalDate dateValidation;
    private Double montant;
    private Integer duree;   // durée en mois
    private Double taux;     // taux en pourcentage

    /**
     * Relation ManyToOne vers Client.
     * Contrat est le côté "propriétaire" de la relation (il porte la FK client_id).
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "client_id", nullable = false)
    private Client client;

    /**
     * Un contrat peut avoir plusieurs paiements associés.
     */
    @OneToMany(mappedBy = "contrat", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<Paiement> paiements;
}
