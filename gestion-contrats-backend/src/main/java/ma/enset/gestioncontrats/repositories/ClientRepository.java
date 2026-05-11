package ma.enset.gestioncontrats.repositories;

import ma.enset.gestioncontrats.entities.Client;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

/**
 * Repository Client.
 * JpaRepository<Client, Long> nous donne gratuitement :
 * save(), findById(), findAll(), deleteById(), count(), existsById()...
 * + support de la pagination avec Pageable.
 */
public interface ClientRepository extends JpaRepository<Client, Long> {

    // Spring Data génère le SQL automatiquement depuis le nom de la méthode
    Optional<Client> findByEmail(String email);

    // Recherche par nom OU prénom (insensible à la casse) - utile pour la recherche côté frontend
    Page<Client> findByNomContainingIgnoreCaseOrPrenomContainingIgnoreCase(
            String nom, String prenom, Pageable pageable);

    // Vérifier si un email est déjà utilisé
    boolean existsByEmail(String email);

    // JPQL custom : chercher un client avec ses contrats chargés (évite le N+1)
    @Query("SELECT c FROM Client c LEFT JOIN FETCH c.contrats WHERE c.id = :id")
    Optional<Client> findByIdWithContrats(@Param("id") Long id);
}
