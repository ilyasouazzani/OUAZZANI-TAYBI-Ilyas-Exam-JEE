package ma.enset.gestioncontrats.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import ma.enset.gestioncontrats.dtos.PaiementRequestDTO;
import ma.enset.gestioncontrats.dtos.PaiementResponseDTO;
import ma.enset.gestioncontrats.services.PaiementService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * REST Controller pour la gestion des paiements.
 * Préfixe : /api/paiements
 */
@RestController
@RequestMapping("/api/paiements")
@RequiredArgsConstructor
@Tag(name = "Paiements", description = "API de gestion des paiements")
@CrossOrigin(origins = "*")
public class PaiementController {

    private final PaiementService paiementService;

    /**
     * POST /api/paiements
     * Enregistrer un nouveau paiement
     */
    @PostMapping
    @Operation(summary = "Enregistrer un paiement pour un contrat validé")
    public ResponseEntity<PaiementResponseDTO> ajouterPaiement(
            @Valid @RequestBody PaiementRequestDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(paiementService.ajouterPaiement(dto));
    }

    /**
     * GET /api/paiements/contrat/{contratId}
     * Tous les paiements d'un contrat
     */
    @GetMapping("/contrat/{contratId}")
    @Operation(summary = "Paiements d'un contrat donné")
    public ResponseEntity<List<PaiementResponseDTO>> getPaiementsByContrat(
            @PathVariable Long contratId) {
        return ResponseEntity.ok(paiementService.getPaiementsByContrat(contratId));
    }

    /**
     * GET /api/paiements/client/{clientId}
     * Tous les paiements d'un client (tous contrats confondus)
     */
    @GetMapping("/client/{clientId}")
    @Operation(summary = "Paiements d'un client (tous contrats confondus)")
    public ResponseEntity<List<PaiementResponseDTO>> getPaiementsByClient(
            @PathVariable Long clientId) {
        return ResponseEntity.ok(paiementService.getPaiementsByClient(clientId));
    }

    /**
     * GET /api/paiements/contrat/{contratId}/total
     * Somme totale payée pour un contrat
     */
    @GetMapping("/contrat/{contratId}/total")
    @Operation(summary = "Total des paiements pour un contrat")
    public ResponseEntity<Map<String, Double>> getTotalPaye(
            @PathVariable Long contratId) {
        Double total = paiementService.getTotalPaye(contratId);
        return ResponseEntity.ok(Map.of("totalPaye", total));
    }
}
