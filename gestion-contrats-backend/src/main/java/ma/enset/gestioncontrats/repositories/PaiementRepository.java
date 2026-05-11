package ma.enset.gestioncontrats.repositories;

import ma.enset.gestioncontrats.entities.Paiement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * Repository Paiement.
 */
public interface PaiementRepository extends JpaRepository<Paiement, Long> {

    // Tous les paiements d'un contrat donné
    List<Paiement> findByContratId(Long contratId);

    // Somme totale payée pour un contrat (JPQL avec agrégation)
    @Query("SELECT COALESCE(SUM(p.montant), 0) FROM Paiement p WHERE p.contrat.id = :contratId")
    Double sumMontantByContratId(@Param("contratId") Long contratId);

    // Paiements d'un client (jointure via contrat)
    @Query("SELECT p FROM Paiement p WHERE p.contrat.client.id = :clientId")
    List<Paiement> findByClientId(@Param("clientId") Long clientId);
}
