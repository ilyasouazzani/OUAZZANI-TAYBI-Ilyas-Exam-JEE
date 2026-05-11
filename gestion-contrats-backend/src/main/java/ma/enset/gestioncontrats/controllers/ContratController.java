package ma.enset.gestioncontrats.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import ma.enset.gestioncontrats.dtos.*;
import ma.enset.gestioncontrats.entities.StatutContrat;
import ma.enset.gestioncontrats.services.ContratService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * REST Controller pour la gestion des contrats.
 * Préfixe : /api/contrats
 */
@RestController
@RequestMapping("/api/contrats")
@RequiredArgsConstructor
@Tag(name = "Contrats", description = "API de gestion des contrats d'assurance")
@CrossOrigin(origins = "*")
public class ContratController {

    private final ContratService contratService;

    /**
     * GET /api/contrats?page=0&size=10
     */
    @GetMapping
    @Operation(summary = "Liste paginée de tous les contrats")
    public ResponseEntity<Page<ContratResponseDTO>> getAllContrats(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        return ResponseEntity.ok(
                contratService.getAllContrats(
                        PageRequest.of(page, size, Sort.by("dateSouscription").descending())
                )
        );
    }

    /**
     * GET /api/contrats/{id}
     */
    @GetMapping("/{id}")
    @Operation(summary = "Récupérer un contrat par son ID")
    public ResponseEntity<ContratResponseDTO> getContratById(@PathVariable Long id) {
        return ResponseEntity.ok(contratService.getContratById(id));
    }

    /**
     * GET /api/contrats/client/{clientId}?page=0&size=10
     */
    @GetMapping("/client/{clientId}")
    @Operation(summary = "Contrats d'un client donné")
    public ResponseEntity<Page<ContratResponseDTO>> getContratsByClient(
            @PathVariable Long clientId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        return ResponseEntity.ok(
                contratService.getContratsByClient(clientId, PageRequest.of(page, size))
        );
    }

    /**
     * GET /api/contrats/statut/VALIDE?page=0&size=10
     */
    @GetMapping("/statut/{statut}")
    @Operation(summary = "Contrats filtrés par statut (BROUILLON, VALIDE, RESILIE...)")
    public ResponseEntity<Page<ContratResponseDTO>> getContratsByStatut(
            @PathVariable StatutContrat statut,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        return ResponseEntity.ok(
                contratService.getContratsByStatut(statut, PageRequest.of(page, size))
        );
    }

    // ---- Création des 3 types de contrats ----

    @PostMapping("/auto")
    @Operation(summary = "Créer un contrat automobile")
    public ResponseEntity<ContratResponseDTO> creerContratAuto(
            @Valid @RequestBody ContratAutoRequestDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(contratService.creerContratAuto(dto));
    }

    @PostMapping("/habitation")
    @Operation(summary = "Créer un contrat habitation")
    public ResponseEntity<ContratResponseDTO> creerContratHabitation(
            @Valid @RequestBody ContratHabitationRequestDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(contratService.creerContratHabitation(dto));
    }

    @PostMapping("/sante")
    @Operation(summary = "Créer un contrat santé")
    public ResponseEntity<ContratResponseDTO> creerContratSante(
            @Valid @RequestBody ContratSanteRequestDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(contratService.creerContratSante(dto));
    }

    // ---- Actions métier ----

    /**
     * PUT /api/contrats/{id}/valider
     */
    @PutMapping("/{id}/valider")
    @Operation(summary = "Valider un contrat (BROUILLON/EN_ATTENTE → VALIDE)")
    public ResponseEntity<ContratResponseDTO> validerContrat(@PathVariable Long id) {
        return ResponseEntity.ok(contratService.validerContrat(id));
    }

    /**
     * PUT /api/contrats/{id}/resilier
     */
    @PutMapping("/{id}/resilier")
    @Operation(summary = "Résilier un contrat (VALIDE → RESILIE)")
    public ResponseEntity<ContratResponseDTO> resilierContrat(@PathVariable Long id) {
        return ResponseEntity.ok(contratService.resilierContrat(id));
    }

    /**
     * DELETE /api/contrats/{id}
     */
    @DeleteMapping("/{id}")
    @Operation(summary = "Supprimer un contrat")
    public ResponseEntity<Void> supprimerContrat(@PathVariable Long id) {
        contratService.supprimerContrat(id);
        return ResponseEntity.noContent().build();
    }
}
