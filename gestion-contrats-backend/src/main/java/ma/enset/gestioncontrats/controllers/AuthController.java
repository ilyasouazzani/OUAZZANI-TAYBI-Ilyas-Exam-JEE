package ma.enset.gestioncontrats.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import ma.enset.gestioncontrats.dtos.LoginRequestDTO;
import ma.enset.gestioncontrats.dtos.LoginResponseDTO;
import ma.enset.gestioncontrats.dtos.RegisterRequestDTO;
import ma.enset.gestioncontrats.entities.AppUser;
import ma.enset.gestioncontrats.repositories.AppUserRepository;
import ma.enset.gestioncontrats.security.JwtUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Set;

/**
 * Controller d'authentification — routes publiques.
 */
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Tag(name = "Authentification", description = "Login et inscription")
@CrossOrigin(origins = "*")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtUtils jwtUtils;
    private final AppUserRepository appUserRepository;
    private final PasswordEncoder passwordEncoder;

    /**
     * POST /api/auth/login
     * Body : { "username": "admin", "password": "1234" }
     * Retourne : { "token": "eyJ...", "username": "admin", "roles": ["ROLE_ADMIN"] }
     */
    @PostMapping("/login")
    @Operation(summary = "Connexion — retourne un token JWT")
    public ResponseEntity<LoginResponseDTO> login(
            @Valid @RequestBody LoginRequestDTO dto) {

        // 1. Spring vérifie username + password (via UserDetailsService + BCrypt)
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(dto.getUsername(), dto.getPassword())
        );

        // 2. Récupérer le UserDetails authentifié
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();

        // 3. Générer le token JWT
        String token = jwtUtils.generateToken(userDetails);

        // 4. Récupérer les rôles depuis la BDD pour les inclure dans la réponse
        AppUser appUser = appUserRepository.findByUsername(dto.getUsername()).orElseThrow();

        return ResponseEntity.ok(
                new LoginResponseDTO(token, userDetails.getUsername(), appUser.getRoles())
        );
    }

    /**
     * POST /api/auth/register
     * Crée un nouveau compte utilisateur.
     */
    @PostMapping("/register")
    @Operation(summary = "Inscription — créer un nouveau compte")
    public ResponseEntity<Map<String, String>> register(
            @Valid @RequestBody RegisterRequestDTO dto) {

        if (appUserRepository.existsByUsername(dto.getUsername())) {
            return ResponseEntity.badRequest()
                    .body(Map.of("message", "Ce nom d'utilisateur est déjà pris"));
        }

        // Déterminer le rôle (par défaut CLIENT)
        String role = "ROLE_CLIENT";
        if (dto.getRole() != null) {
            role = "ROLE_" + dto.getRole().toUpperCase();
        }

        AppUser newUser = AppUser.builder()
                .username(dto.getUsername())
                .password(passwordEncoder.encode(dto.getPassword())) // hashage BCrypt
                .email(dto.getEmail())
                .enabled(true)
                .roles(Set.of(role))
                .build();

        appUserRepository.save(newUser);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(Map.of("message", "Compte créé avec succès"));
    }
}
