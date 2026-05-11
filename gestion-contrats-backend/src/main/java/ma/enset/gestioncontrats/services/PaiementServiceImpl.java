package ma.enset.gestioncontrats.services;

import ma.enset.gestioncontrats.dtos.PaiementRequestDTO;
import ma.enset.gestioncontrats.dtos.PaiementResponseDTO;
import ma.enset.gestioncontrats.entities.Contrat;
import ma.enset.gestioncontrats.entities.Paiement;
import ma.enset.gestioncontrats.entities.StatutContrat;
import ma.enset.gestioncontrats.mappers.PaiementMapper;
import ma.enset.gestioncontrats.repositories.ContratRepository;
import ma.enset.gestioncontrats.repositories.PaiementRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class PaiementServiceImpl implements PaiementService {

    private final PaiementRepository paiementRepository;
    private final ContratRepository contratRepository;
    private final PaiementMapper paiementMapper;

    @Override
    public PaiementResponseDTO ajouterPaiement(PaiementRequestDTO dto) {
        Contrat contrat = contratRepository.findById(dto.getContratId())
                .orElseThrow(() -> new RuntimeException("Contrat introuvable : " + dto.getContratId()));

        // On ne peut payer que sur un contrat validé
        if (contrat.getStatut() != StatutContrat.VALIDE) {
            throw new RuntimeException("Le paiement n'est possible que sur un contrat validé");
        }

        Paiement paiement = Paiement.builder()
                .contrat(contrat)
                .montant(dto.getMontant())
                .modePaiement(dto.getModePaiement())
                .datePaiement(dto.getDatePaiement() != null ? dto.getDatePaiement() : LocalDate.now())
                .build();

        return paiementMapper.toResponseDTO(paiementRepository.save(paiement));
    }

    @Override
    @Transactional(readOnly = true)
    public List<PaiementResponseDTO> getPaiementsByContrat(Long contratId) {
        return paiementRepository.findByContratId(contratId)
                .stream()
                .map(paiementMapper::toResponseDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<PaiementResponseDTO> getPaiementsByClient(Long clientId) {
        return paiementRepository.findByClientId(clientId)
                .stream()
                .map(paiementMapper::toResponseDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public Double getTotalPaye(Long contratId) {
        return paiementRepository.sumMontantByContratId(contratId);
    }
}
