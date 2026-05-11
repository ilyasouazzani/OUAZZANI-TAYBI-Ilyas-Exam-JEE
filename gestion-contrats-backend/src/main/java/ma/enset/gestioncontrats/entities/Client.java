package ma.enset.gestioncontrats.entities;

import jakarta.persistence.*;
import lombok.*;
import java.util.List;

/**
 * Entité Client - représente un assuré dans le système.
 * Un client peut posséder plusieurs contrats (1..N).
 */
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Client {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nom;
    private String prenom;
    private String email;
    private String telephone;

    /**
     * mappedBy = "client" signifie que c'est la classe Contrat
     * qui porte la clé étrangère (client_id), pas Client.
     * cascade = ALL : suppression d'un client => suppression de ses contrats.
     * fetch = LAZY : les contrats ne sont chargés que si on y accède explicitement.
     */
    @OneToMany(mappedBy = "client", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<Contrat> contrats;
}
