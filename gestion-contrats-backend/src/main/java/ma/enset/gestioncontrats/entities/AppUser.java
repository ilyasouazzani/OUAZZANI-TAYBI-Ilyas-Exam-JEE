package ma.enset.gestioncontrats.entities;

import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

/**
 * Entité représentant un utilisateur de l'application.
 *
 * @ElementCollection(fetch = EAGER) : les rôles sont stockés dans une table
 * séparée "app_user_roles" (créée automatiquement par JPA).
 * EAGER = les rôles sont chargés en même temps que l'user (nécessaire pour Spring Security).
 *
 * Rôles possibles : ROLE_CLIENT, ROLE_EMPLOYE, ROLE_ADMIN
 * (la convention Spring Security exige le préfixe ROLE_)
 */
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AppUser {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String username;

    @Column(nullable = false)
    private String password; // stocké hashé (BCrypt)

    private String email;
    private boolean enabled = true;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(
        name = "app_user_roles",
        joinColumns = @JoinColumn(name = "user_id")
    )
    @Column(name = "role")
    @Builder.Default
    private Set<String> roles = new HashSet<>();
}
