package ma.enset.gestioncontrats.services;

import ma.enset.gestioncontrats.dtos.*;
import ma.enset.gestioncontrats.entities.StatutContrat;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ContratService {

    // Création des 3 types
    ContratResponseDTO creerContratAuto(ContratAutoRequestDTO dto);
    ContratResponseDTO creerContratHabitation(ContratHabitationRequestDTO dto);
    ContratResponseDTO creerContratSante(ContratSanteRequestDTO dto);

    // Lecture
    ContratResponseDTO getContratById(Long id);
    Page<ContratResponseDTO> getAllContrats(Pageable pageable);
    Page<ContratResponseDTO> getContratsByClient(Long clientId, Pageable pageable);
    Page<ContratResponseDTO> getContratsByStatut(StatutContrat statut, Pageable pageable);

    // Changement de statut (logique métier)
    ContratResponseDTO validerContrat(Long id);
    ContratResponseDTO resilierContrat(Long id);

    void supprimerContrat(Long id);
}
