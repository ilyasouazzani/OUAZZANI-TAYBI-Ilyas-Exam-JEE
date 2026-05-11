package ma.enset.gestioncontrats.mappers;

import ma.enset.gestioncontrats.dtos.*;
import ma.enset.gestioncontrats.entities.*;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

/**
 * Mapper pour tous les types de contrats.
 *
 * La méthode toResponseDTO utilise instanceof pour détecter le vrai type
 * et remplir les champs spécifiques. C'est le polymorphisme Java côté service.
 */
@Component
public class ContratMapper {

    /** Entité Contrat (n'importe quel sous-type) → ResponseDTO */
    public ContratResponseDTO toResponseDTO(Contrat contrat) {
        if (contrat == null) return null;

        ContratResponseDTO dto = new ContratResponseDTO();
        // Champs communs
        dto.setId(contrat.getId());
        dto.setDateSouscription(contrat.getDateSouscription());
        dto.setStatut(contrat.getStatut());
        dto.setDateValidation(contrat.getDateValidation());
        dto.setMontant(contrat.getMontant());
        dto.setDuree(contrat.getDuree());
        dto.setTaux(contrat.getTaux());

        // Infos client
        if (contrat.getClient() != null) {
            dto.setClientId(contrat.getClient().getId());
            dto.setClientNomComplet(
                contrat.getClient().getPrenom() + " " + contrat.getClient().getNom()
            );
        }

        // Champs spécifiques selon le vrai type
        if (contrat instanceof ContratAuto auto) {
            dto.setTypeContrat("AUTO");
            dto.setImmatriculation(auto.getImmatriculation());
            dto.setMarque(auto.getMarque());
            dto.setModele(auto.getModele());

        } else if (contrat instanceof ContratHabitation hab) {
            dto.setTypeContrat("HABITATION");
            dto.setType(hab.getType());
            dto.setAdresse(hab.getAdresse());
            dto.setSuperficie(hab.getSuperficie());

        } else if (contrat instanceof ContratSante sante) {
            dto.setTypeContrat("SANTE");
            dto.setNiveauCouverture(sante.getNiveauCouverture());
            dto.setNbPersonnes(sante.getNbPersonnes());
        }

        return dto;
    }

    /** RequestDTO Auto → Entité ContratAuto */
    public ContratAuto toContratAutoEntity(ContratAutoRequestDTO dto, Client client) {
        ContratAuto contrat = new ContratAuto();
        contrat.setClient(client);
        contrat.setDateSouscription(LocalDate.now());
        contrat.setStatut(StatutContrat.BROUILLON);
        contrat.setMontant(dto.getMontant());
        contrat.setDuree(dto.getDuree());
        contrat.setTaux(dto.getTaux());
        contrat.setImmatriculation(dto.getImmatriculation());
        contrat.setMarque(dto.getMarque());
        contrat.setModele(dto.getModele());
        return contrat;
    }

    /** RequestDTO Habitation → Entité ContratHabitation */
    public ContratHabitation toContratHabitationEntity(ContratHabitationRequestDTO dto, Client client) {
        ContratHabitation contrat = new ContratHabitation();
        contrat.setClient(client);
        contrat.setDateSouscription(LocalDate.now());
        contrat.setStatut(StatutContrat.BROUILLON);
        contrat.setMontant(dto.getMontant());
        contrat.setDuree(dto.getDuree());
        contrat.setTaux(dto.getTaux());
        contrat.setType(dto.getType());
        contrat.setAdresse(dto.getAdresse());
        contrat.setSuperficie(dto.getSuperficie());
        return contrat;
    }

    /** RequestDTO Santé → Entité ContratSante */
    public ContratSante toContratSanteEntity(ContratSanteRequestDTO dto, Client client) {
        ContratSante contrat = new ContratSante();
        contrat.setClient(client);
        contrat.setDateSouscription(LocalDate.now());
        contrat.setStatut(StatutContrat.BROUILLON);
        contrat.setMontant(dto.getMontant());
        contrat.setDuree(dto.getDuree());
        contrat.setTaux(dto.getTaux());
        contrat.setNiveauCouverture(dto.getNiveauCouverture());
        contrat.setNbPersonnes(dto.getNbPersonnes());
        return contrat;
    }
}
