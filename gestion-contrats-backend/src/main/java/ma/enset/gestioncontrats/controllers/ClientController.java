package ma.enset.gestioncontrats.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import ma.enset.gestioncontrats.dtos.ClientRequestDTO;
import ma.enset.gestioncontrats.dtos.ClientResponseDTO;
import ma.enset.gestioncontrats.services.ClientService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * REST Controller pour la gestion des clients.
 * Préfixe : /api/clients
 */
@RestController
@RequestMapping("/api/clients")
@RequiredArgsConstructor
@Tag(name = "Clients", description = "API de gestion des clients")
@CrossOrigin(origins = "*") // Autorise Angular (localhost:4200) à appeler cette API
public class ClientController {

    private final ClientService clientService;

    /**
     * GET /api/clients?page=0&size=10&sortBy=nom
     * Liste paginée de tous les clients
     */
    @GetMapping
    @Operation(summary = "Liste paginée des clients")
    public ResponseEntity<Page<ClientResponseDTO>> getAllClients(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "nom") String sortBy) {

        Page<ClientResponseDTO> clients = clientService.getAllClients(
                PageRequest.of(page, size, Sort.by(sortBy))
        );
        return ResponseEntity.ok(clients);
    }

    /**
     * GET /api/clients/search?keyword=sara&page=0&size=10
     * Recherche par nom ou prénom
     */
    @GetMapping("/search")
    @Operation(summary = "Recherche clients par nom ou prénom")
    public ResponseEntity<Page<ClientResponseDTO>> searchClients(
            @RequestParam String keyword,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        return ResponseEntity.ok(
                clientService.rechercherClients(keyword, PageRequest.of(page, size))
        );
    }

    /**
     * GET /api/clients/{id}
     */
    @GetMapping("/{id}")
    @Operation(summary = "Récupérer un client par son ID")
    public ResponseEntity<ClientResponseDTO> getClientById(@PathVariable Long id) {
        return ResponseEntity.ok(clientService.getClientById(id));
    }

    /**
     * POST /api/clients
     * Créer un nouveau client — retourne 201 Created
     */
    @PostMapping
    @Operation(summary = "Créer un nouveau client")
    public ResponseEntity<ClientResponseDTO> creerClient(
            @Valid @RequestBody ClientRequestDTO dto) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(clientService.creerClient(dto));
    }

    /**
     * PUT /api/clients/{id}
     * Modifier un client existant
     */
    @PutMapping("/{id}")
    @Operation(summary = "Modifier un client")
    public ResponseEntity<ClientResponseDTO> modifierClient(
            @PathVariable Long id,
            @Valid @RequestBody ClientRequestDTO dto) {
        return ResponseEntity.ok(clientService.modifierClient(id, dto));
    }

    /**
     * DELETE /api/clients/{id}
     * Supprimer un client — retourne 204 No Content
     */
    @DeleteMapping("/{id}")
    @Operation(summary = "Supprimer un client")
    public ResponseEntity<Void> supprimerClient(@PathVariable Long id) {
        clientService.supprimerClient(id);
        return ResponseEntity.noContent().build();
    }
}
