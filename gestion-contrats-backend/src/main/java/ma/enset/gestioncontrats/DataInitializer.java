package ma.enset.gestioncontrats;

import ma.enset.gestioncontrats.entities.*;
import ma.enset.gestioncontrats.repositories.*;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

/**
 * Peuple la base H2 avec des données réalistes au démarrage.
 * Exécuté une seule fois après le démarrage de Spring Boot.
 *
 * @RequiredArgsConstructor = Lombok génère un constructeur avec tous les champs final
 * (c'est l'injection de dépendances par constructeur, recommandée par Spring)
 */
@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final ClientRepository clientRepository;
    private final ContratAutoRepository contratAutoRepository;
    private final ContratHabitationRepository contratHabitationRepository;
    private final ContratSanteRepository contratSanteRepository;
    private final PaiementRepository paiementRepository;

    @Override
    public void run(String... args) throws Exception {

        // =============================================
        // 1. CRÉATION DES CLIENTS
        // =============================================
        Client client1 = clientRepository.save(Client.builder()
                .nom("Ouazzani").prenom("Ilyas")
                .email("ilyas.ouazzani@gmail.com")
                .telephone("0612345678")
                .build());

        Client client2 = clientRepository.save(Client.builder()
                .nom("Benali").prenom("Sara")
                .email("sara.benali@gmail.com")
                .telephone("0698765432")
                .build());

        Client client3 = clientRepository.save(Client.builder()
                .nom("Tazi").prenom("Mehdi")
                .email("mehdi.tazi@gmail.com")
                .telephone("0665544332")
                .build());

        System.out.println("✓ 3 clients créés");

        // =============================================
        // 2. CONTRATS AUTO
        // =============================================
        ContratAuto auto1 = new ContratAuto();
        auto1.setClient(client1);
        auto1.setDateSouscription(LocalDate.of(2024, 1, 15));
        auto1.setStatut(StatutContrat.VALIDE);
        auto1.setDateValidation(LocalDate.of(2024, 1, 20));
        auto1.setMontant(3500.0);
        auto1.setDuree(12);
        auto1.setTaux(5.5);
        auto1.setImmatriculation("123-A-45");
        auto1.setMarque("Toyota");
        auto1.setModele("Corolla");
        contratAutoRepository.save(auto1);

        ContratAuto auto2 = new ContratAuto();
        auto2.setClient(client2);
        auto2.setDateSouscription(LocalDate.of(2024, 3, 10));
        auto2.setStatut(StatutContrat.EN_ATTENTE);
        auto2.setMontant(4200.0);
        auto2.setDuree(24);
        auto2.setTaux(6.0);
        auto2.setImmatriculation("456-B-78");
        auto2.setMarque("Dacia");
        auto2.setModele("Duster");
        contratAutoRepository.save(auto2);

        System.out.println("✓ 2 contrats auto créés");

        // =============================================
        // 3. CONTRATS HABITATION
        // =============================================
        ContratHabitation hab1 = new ContratHabitation();
        hab1.setClient(client2);
        hab1.setDateSouscription(LocalDate.of(2023, 6, 1));
        hab1.setStatut(StatutContrat.VALIDE);
        hab1.setDateValidation(LocalDate.of(2023, 6, 5));
        hab1.setMontant(1800.0);
        hab1.setDuree(12);
        hab1.setTaux(3.2);
        hab1.setType("Appartement");
        hab1.setAdresse("12 Rue Hassan II, Casablanca");
        hab1.setSuperficie(85.0);
        contratHabitationRepository.save(hab1);

        ContratHabitation hab2 = new ContratHabitation();
        hab2.setClient(client3);
        hab2.setDateSouscription(LocalDate.of(2024, 2, 20));
        hab2.setStatut(StatutContrat.VALIDE);
        hab2.setDateValidation(LocalDate.of(2024, 2, 25));
        hab2.setMontant(2500.0);
        hab2.setDuree(12);
        hab2.setTaux(3.8);
        hab2.setType("Villa");
        hab2.setAdresse("5 Avenue Mohammed V, Rabat");
        hab2.setSuperficie(220.0);
        contratHabitationRepository.save(hab2);

        System.out.println("✓ 2 contrats habitation créés");

        // =============================================
        // 4. CONTRATS SANTÉ
        // =============================================
        ContratSante sante1 = new ContratSante();
        sante1.setClient(client1);
        sante1.setDateSouscription(LocalDate.of(2024, 1, 15));
        sante1.setStatut(StatutContrat.VALIDE);
        sante1.setDateValidation(LocalDate.of(2024, 1, 16));
        sante1.setMontant(2400.0);
        sante1.setDuree(12);
        sante1.setTaux(4.0);
        sante1.setNiveauCouverture("PREMIUM");
        sante1.setNbPersonnes(3);
        contratSanteRepository.save(sante1);

        ContratSante sante2 = new ContratSante();
        sante2.setClient(client3);
        sante2.setDateSouscription(LocalDate.of(2023, 11, 1));
        sante2.setStatut(StatutContrat.RESILIE);
        sante2.setDateValidation(LocalDate.of(2023, 11, 3));
        sante2.setMontant(1200.0);
        sante2.setDuree(6);
        sante2.setTaux(3.0);
        sante2.setNiveauCouverture("BASIQUE");
        sante2.setNbPersonnes(1);
        contratSanteRepository.save(sante2);

        System.out.println("✓ 2 contrats santé créés");

        // =============================================
        // 5. PAIEMENTS (pour les contrats validés)
        // =============================================
        paiementRepository.save(Paiement.builder()
                .contrat(auto1)
                .datePaiement(LocalDate.of(2024, 2, 1))
                .montant(291.67)
                .modePaiement("VIREMENT")
                .build());

        paiementRepository.save(Paiement.builder()
                .contrat(auto1)
                .datePaiement(LocalDate.of(2024, 3, 1))
                .montant(291.67)
                .modePaiement("VIREMENT")
                .build());

        paiementRepository.save(Paiement.builder()
                .contrat(hab1)
                .datePaiement(LocalDate.of(2023, 7, 1))
                .montant(150.0)
                .modePaiement("CARTE")
                .build());

        paiementRepository.save(Paiement.builder()
                .contrat(sante1)
                .datePaiement(LocalDate.of(2024, 2, 15))
                .montant(200.0)
                .modePaiement("CHEQUE")
                .build());

        System.out.println("✓ 4 paiements créés");
        System.out.println("========================================");
        System.out.println("  Base de données initialisée avec succès");
        System.out.println("  Clients : " + clientRepository.count());
        System.out.println("  Contrats Auto : " + contratAutoRepository.count());
        System.out.println("  Contrats Habitation : " + contratHabitationRepository.count());
        System.out.println("  Contrats Santé : " + contratSanteRepository.count());
        System.out.println("  Paiements : " + paiementRepository.count());
        System.out.println("========================================");
    }
}
