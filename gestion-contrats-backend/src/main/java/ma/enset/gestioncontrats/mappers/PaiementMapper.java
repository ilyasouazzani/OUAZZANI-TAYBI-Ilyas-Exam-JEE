package ma.enset.gestioncontrats.mappers;

import ma.enset.gestioncontrats.dtos.PaiementResponseDTO;
import ma.enset.gestioncontrats.entities.ContratAuto;
import ma.enset.gestioncontrats.entities.ContratHabitation;
import ma.enset.gestioncontrats.entities.ContratSante;
import ma.enset.gestioncontrats.entities.Paiement;
import org.springframework.stereotype.Component;

@Component
public class PaiementMapper {

    public PaiementResponseDTO toResponseDTO(Paiement paiement) {
        if (paiement == null) return null;
        PaiementResponseDTO dto = new PaiementResponseDTO();
        dto.setId(paiement.getId());
        dto.setDatePaiement(paiement.getDatePaiement());
        dto.setMontant(paiement.getMontant());
        dto.setModePaiement(paiement.getModePaiement());

        if (paiement.getContrat() != null) {
            dto.setContratId(paiement.getContrat().getId());
            // Déterminer le type pour le frontend
            if (paiement.getContrat() instanceof ContratAuto) dto.setTypeContrat("AUTO");
            else if (paiement.getContrat() instanceof ContratHabitation) dto.setTypeContrat("HABITATION");
            else if (paiement.getContrat() instanceof ContratSante) dto.setTypeContrat("SANTE");
        }
        return dto;
    }
}
