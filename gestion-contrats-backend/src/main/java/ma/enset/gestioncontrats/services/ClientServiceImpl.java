package ma.enset.gestioncontrats.services;

import ma.enset.gestioncontrats.dtos.ClientRequestDTO;
import ma.enset.gestioncontrats.dtos.ClientResponseDTO;
import ma.enset.gestioncontrats.entities.Client;
import ma.enset.gestioncontrats.mappers.ClientMapper;
import ma.enset.gestioncontrats.repositories.ClientRepository;
import ma.enset.gestioncontrats.repositories.ContratRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Implémentation du service Client.
 *
 * @Transactional : toutes les méthodes s'exécutent dans une transaction JPA.
 * Si une exception est levée, la transaction est annulée (rollback automatique).
 *
 * @RequiredArgsConstructor : injection par constructeur (meilleure pratique Spring).
 */
@Service
@Transactional
@RequiredArgsConstructor
public class ClientServiceImpl implements ClientService {

    private final ClientRepository clientRepository;
    private final ContratRepository contratRepository;
    private final ClientMapper clientMapper;

    @Override
    public ClientResponseDTO creerClient(ClientRequestDTO dto) {
        // Vérification doublon email
        if (clientRepository.existsByEmail(dto.getEmail())) {
            throw new RuntimeException("Un client avec l'email " + dto.getEmail() + " existe déjà");
        }
        Client client = clientMapper.toEntity(dto);
        Client saved = clientRepository.save(client);
        ClientResponseDTO response = clientMapper.toResponseDTO(saved);
        response.setNombreContrats(0);
        return response;
    }

    @Override
    public ClientResponseDTO modifierClient(Long id, ClientRequestDTO dto) {
        Client client = clientRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Client introuvable avec l'id : " + id));
        clientMapper.updateEntityFromDTO(dto, client);
        Client updated = clientRepository.save(client);
        ClientResponseDTO response = clientMapper.toResponseDTO(updated);
        response.setNombreContrats(contratRepository.countByClientId(id));
        return response;
    }

    @Override
    @Transactional(readOnly = true)
    public ClientResponseDTO getClientById(Long id) {
        Client client = clientRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Client introuvable avec l'id : " + id));
        ClientResponseDTO response = clientMapper.toResponseDTO(client);
        response.setNombreContrats(contratRepository.countByClientId(id));
        return response;
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ClientResponseDTO> getAllClients(Pageable pageable) {
        return clientRepository.findAll(pageable)
                .map(client -> {
                    ClientResponseDTO dto = clientMapper.toResponseDTO(client);
                    dto.setNombreContrats(contratRepository.countByClientId(client.getId()));
                    return dto;
                });
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ClientResponseDTO> rechercherClients(String keyword, Pageable pageable) {
        return clientRepository
                .findByNomContainingIgnoreCaseOrPrenomContainingIgnoreCase(keyword, keyword, pageable)
                .map(client -> {
                    ClientResponseDTO dto = clientMapper.toResponseDTO(client);
                    dto.setNombreContrats(contratRepository.countByClientId(client.getId()));
                    return dto;
                });
    }

    @Override
    public void supprimerClient(Long id) {
        if (!clientRepository.existsById(id)) {
            throw new RuntimeException("Client introuvable avec l'id : " + id);
        }
        clientRepository.deleteById(id);
    }
}
