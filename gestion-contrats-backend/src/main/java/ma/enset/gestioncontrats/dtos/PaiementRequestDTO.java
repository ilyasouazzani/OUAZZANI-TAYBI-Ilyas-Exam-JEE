package ma.enset.gestioncontrats.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

import java.time.LocalDate;

@Data
public class PaiementRequestDTO {

    @NotNull
    private Long contratId;

    @NotNull @Positive
    private Double montant;

    @NotBlank
    private String modePaiement; // VIREMENT, CARTE, CHEQUE, ESPECES

    private LocalDate datePaiement; // si null, on prend LocalDate.now()
}
