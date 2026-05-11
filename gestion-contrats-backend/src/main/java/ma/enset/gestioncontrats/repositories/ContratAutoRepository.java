package ma.enset.gestioncontrats.repositories;

import ma.enset.gestioncontrats.entities.ContratAuto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Repository spécifique pour ContratAuto.
 * Hérite de toutes les méthodes de JpaRepository,
 * mais ne retourne que des ContratAuto (pas d'habitation ni santé).
 */
public interface ContratAutoRepository extends JpaRepository<ContratAuto, Long> {

    // Recherche par immatriculation (utile pour vérifier les doublons)
    ContratAuto findByImmatriculation(String immatriculation);

    // Recherche par marque avec pagination
    Page<ContratAuto> findByMarqueContainingIgnoreCase(String marque, Pageable pageable);
}
