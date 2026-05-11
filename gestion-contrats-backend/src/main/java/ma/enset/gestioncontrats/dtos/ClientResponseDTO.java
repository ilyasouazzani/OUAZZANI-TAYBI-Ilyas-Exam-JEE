package ma.enset.gestioncontrats.dtos;

import lombok.Data;

/**
 * DTO renvoyé au frontend après création/lecture d'un client.
 * Pas de liste de contrats ici pour éviter les données trop lourdes.
 * Si on veut les contrats d'un client, on appellera un endpoint dédié.
 */
@Data
public class ClientResponseDTO {
    private Long id;
    private String nom;
    private String prenom;
    private String email;
    private String telephone;
    private long nombreContrats; // juste le compteur, pas la liste complète
}
