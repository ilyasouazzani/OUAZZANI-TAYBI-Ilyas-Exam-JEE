package ma.enset.gestioncontrats.repositories;

import ma.enset.gestioncontrats.entities.Contrat;
import ma.enset.gestioncontrats.entities.StatutContrat;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Repository de base pour Contrat (la classe parente abstraite).
 * Grâce au polymorphisme JPA, ce repository peut retourner
 * des ContratAuto, ContratHabitation, ou ContratSante selon les données.
 */
public interface ContratRepository extends JpaRepository<Contrat, Long> {

    // Tous les contrats d'un client (avec pagination pour le frontend)
    Page<Contrat> findByClientId(Long clientId, Pageable pageable);

    // Filtrer par statut
    Page<Contrat> findByStatut(StatutContrat statut, Pageable pageable);

    // Contrats d'un client avec un statut précis
    List<Contrat> findByClientIdAndStatut(Long clientId, StatutContrat statut);

    // Compter les contrats d'un client
    long countByClientId(Long clientId);
}
