package ma.enset.gestioncontrats.repositories;

import ma.enset.gestioncontrats.entities.ContratSante;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Repository spécifique pour ContratSante.
 */
public interface ContratSanteRepository extends JpaRepository<ContratSante, Long> {

    // Filtrer par niveau de couverture (BASIQUE, STANDARD, PREMIUM)
    List<ContratSante> findByNiveauCouverture(String niveauCouverture);

    // Contrats couvrant plus de N personnes
    List<ContratSante> findByNbPersonnesGreaterThan(int nb);
}
