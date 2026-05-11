package ma.enset.gestioncontrats.repositories;

import ma.enset.gestioncontrats.entities.ContratHabitation;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Repository spécifique pour ContratHabitation.
 */
public interface ContratHabitationRepository extends JpaRepository<ContratHabitation, Long> {

    // Recherche par ville/adresse
    Page<ContratHabitation> findByAdresseContainingIgnoreCase(String adresse, Pageable pageable);

    // Filtrer par type de logement (appartement, villa, etc.)
    Page<ContratHabitation> findByType(String type, Pageable pageable);
}
