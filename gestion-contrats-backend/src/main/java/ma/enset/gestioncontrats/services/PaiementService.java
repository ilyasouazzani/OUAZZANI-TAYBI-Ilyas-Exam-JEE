package ma.enset.gestioncontrats.services;

import ma.enset.gestioncontrats.dtos.PaiementRequestDTO;
import ma.enset.gestioncontrats.dtos.PaiementResponseDTO;

import java.util.List;

public interface PaiementService {
    PaiementResponseDTO ajouterPaiement(PaiementRequestDTO dto);
    List<PaiementResponseDTO> getPaiementsByContrat(Long contratId);
    List<PaiementResponseDTO> getPaiementsByClient(Long clientId);
    Double getTotalPaye(Long contratId);
}
