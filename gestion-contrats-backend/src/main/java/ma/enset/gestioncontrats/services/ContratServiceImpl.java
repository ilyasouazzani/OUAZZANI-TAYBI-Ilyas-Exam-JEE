package ma.enset.gestioncontrats.services;

import ma.enset.gestioncontrats.dtos.*;
import ma.enset.gestioncontrats.entities.*;
import ma.enset.gestioncontrats.mappers.ContratMapper;
import ma.enset.gestioncontrats.repositories.*;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

@Service
@Transactional
@RequiredArgsConstructor
public class ContratServiceImpl implements ContratService {

    private final ContratRepository contratRepository;
    private final ContratAutoRepository contratAutoRepository;
    private final ContratHabitationRepository contratHabitationRepository;
    private final ContratSanteRepository contratSanteRepository;
    private final ClientRepository clientRepository;
    private final ContratMapper contratMapper;

    // ---- Helpers ----

    private Client getClientOrThrow(Long clientId) {
        return clientRepository.findById(clientId)
                .orElseThrow(() -> new RuntimeException("Client introuvable : " + clientId));
    }

    private Contrat getContratOrThrow(Long id) {
        return contratRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Contrat introuvable : " + id));
    }

    // ---- Création ----

    @Override
    public ContratResponseDTO creerContratAuto(ContratAutoRequestDTO dto) {
        Client client = getClientOrThrow(dto.getClientId());
        ContratAuto contrat = contratMapper.toContratAutoEntity(dto, client);
        return contratMapper.toResponseDTO(contratAutoRepository.save(contrat));
    }

    @Override
    public ContratResponseDTO creerContratHabitation(ContratHabitationRequestDTO dto) {
        Client client = getClientOrThrow(dto.getClientId());
        ContratHabitation contrat = contratMapper.toContratHabitationEntity(dto, client);
        return contratMapper.toResponseDTO(contratHabitationRepository.save(contrat));
    }

    @Override
    public ContratResponseDTO creerContratSante(ContratSanteRequestDTO dto) {
        Client client = getClientOrThrow(dto.getClientId());
        ContratSante contrat = contratMapper.toContratSanteEntity(dto, client);
        return contratMapper.toResponseDTO(contratSanteRepository.save(contrat));
    }

    // ---- Lecture ----

    @Override
    @Transactional(readOnly = true)
    public ContratResponseDTO getContratById(Long id) {
        return contratMapper.toResponseDTO(getContratOrThrow(id));
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ContratResponseDTO> getAllContrats(Pageable pageable) {
        return contratRepository.findAll(pageable)
                .map(contratMapper::toResponseDTO);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ContratResponseDTO> getContratsByClient(Long clientId, Pageable pageable) {
        return contratRepository.findByClientId(clientId, pageable)
                .map(contratMapper::toResponseDTO);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ContratResponseDTO> getContratsByStatut(StatutContrat statut, Pageable pageable) {
        return contratRepository.findByStatut(statut, pageable)
                .map(contratMapper::toResponseDTO);
    }

    // ---- Logique métier : changements de statut ----

    @Override
    public ContratResponseDTO validerContrat(Long id) {
        Contrat contrat = getContratOrThrow(id);
        if (contrat.getStatut() == StatutContrat.RESILIE) {
            throw new RuntimeException("Impossible de valider un contrat résilié");
        }
        contrat.setStatut(StatutContrat.VALIDE);
        contrat.setDateValidation(LocalDate.now());
        return contratMapper.toResponseDTO(contratRepository.save(contrat));
    }

    @Override
    public ContratResponseDTO resilierContrat(Long id) {
        Contrat contrat = getContratOrThrow(id);
        if (contrat.getStatut() != StatutContrat.VALIDE) {
            throw new RuntimeException("Seul un contrat validé peut être résilié");
        }
        contrat.setStatut(StatutContrat.RESILIE);
        return contratMapper.toResponseDTO(contratRepository.save(contrat));
    }

    @Override
    public void supprimerContrat(Long id) {
        if (!contratRepository.existsById(id)) {
            throw new RuntimeException("Contrat introuvable : " + id);
        }
        contratRepository.deleteById(id);
    }
}
