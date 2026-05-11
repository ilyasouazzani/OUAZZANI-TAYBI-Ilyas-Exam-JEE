package ma.enset.gestioncontrats.dtos;

import lombok.Data;
import java.time.LocalDate;

@Data
public class PaiementResponseDTO {
    private Long id;
    private LocalDate datePaiement;
    private Double montant;
    private String modePaiement;
    private Long contratId;
    private String typeContrat;
}
