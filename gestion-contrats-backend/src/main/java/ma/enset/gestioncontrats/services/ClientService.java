package ma.enset.gestioncontrats.services;

import ma.enset.gestioncontrats.dtos.ClientRequestDTO;
import ma.enset.gestioncontrats.dtos.ClientResponseDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Interface du service Client.
 * On programme contre l'interface, jamais contre l'implémentation.
 * Principe SOLID : Dependency Inversion.
 */
public interface ClientService {

    ClientResponseDTO creerClient(ClientRequestDTO dto);

    ClientResponseDTO modifierClient(Long id, ClientRequestDTO dto);

    ClientResponseDTO getClientById(Long id);

    Page<ClientResponseDTO> getAllClients(Pageable pageable);

    Page<ClientResponseDTO> rechercherClients(String keyword, Pageable pageable);

    void supprimerClient(Long id);
}
