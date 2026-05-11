package ma.enset.gestioncontrats.mappers;

import ma.enset.gestioncontrats.dtos.ClientRequestDTO;
import ma.enset.gestioncontrats.dtos.ClientResponseDTO;
import ma.enset.gestioncontrats.entities.Client;
import org.springframework.stereotype.Component;

/**
 * Mapper manuel Client ↔ DTO.
 * @Component : Spring le gère comme un bean injectable.
 * Logique simple, lisible, explicable en 30 secondes en soutenance.
 */
@Component
public class ClientMapper {

    /** Entité → ResponseDTO (pour renvoyer au frontend) */
    public ClientResponseDTO toResponseDTO(Client client) {
        if (client == null) return null;
        ClientResponseDTO dto = new ClientResponseDTO();
        dto.setId(client.getId());
        dto.setNom(client.getNom());
        dto.setPrenom(client.getPrenom());
        dto.setEmail(client.getEmail());
        dto.setTelephone(client.getTelephone());
        // On compte les contrats sans les charger tous en mémoire
        dto.setNombreContrats(
            client.getContrats() != null ? client.getContrats().size() : 0
        );
        return dto;
    }

    /** RequestDTO → Entité (pour sauvegarder en BDD) */
    public Client toEntity(ClientRequestDTO dto) {
        if (dto == null) return null;
        return Client.builder()
                .nom(dto.getNom())
                .prenom(dto.getPrenom())
                .email(dto.getEmail())
                .telephone(dto.getTelephone())
                .build();
    }

    /** Mise à jour d'une entité existante depuis un RequestDTO */
    public void updateEntityFromDTO(ClientRequestDTO dto, Client client) {
        client.setNom(dto.getNom());
        client.setPrenom(dto.getPrenom());
        client.setEmail(dto.getEmail());
        client.setTelephone(dto.getTelephone());
    }
}
