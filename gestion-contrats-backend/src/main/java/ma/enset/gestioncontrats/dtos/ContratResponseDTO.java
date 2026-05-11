package ma.enset.gestioncontrats.dtos;

import ma.enset.gestioncontrats.entities.StatutContrat;
import lombok.Data;

import java.time.LocalDate;

/**
 * DTO de réponse universel pour tous les types de contrats.
 * Le champ "typeContrat" permet au frontend de savoir de quel type il s'agit.
 * Les champs spécifiques (immatriculation, adresse, etc.) sont présents mais
 * seront null si ce n'est pas le bon type — approche simple pour l'exam.
 */
@Data
public class ContratResponseDTO {

    private Long id;
    private String typeContrat; // "AUTO", "HABITATION", "SANTE"
    private LocalDate dateSouscription;
    private StatutContrat statut;
    private LocalDate dateValidation;
    private Double montant;
    private Integer duree;
    private Double taux;

    // Infos client (résumé)
    private Long clientId;
    private String clientNomComplet;

    // Champs spécifiques AUTO
    private String immatriculation;
    private String marque;
    private String modele;

    // Champs spécifiques HABITATION
    private String type;
    private String adresse;
    private Double superficie;

    // Champs spécifiques SANTE
    private String niveauCouverture;
    private Integer nbPersonnes;
}
