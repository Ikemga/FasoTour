package egate.digital.fasotour.services;

import java.time.Duration;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import egate.digital.fasotour.dto.site.SiteTouristiqueResponseDTO;
import egate.digital.fasotour.mappers.SiteTouristiqueMapper;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import egate.digital.fasotour.dto.site.CircuitRequestDTO;
import egate.digital.fasotour.dto.site.CircuitResponseDTO;
import egate.digital.fasotour.mappers.CircuitMapper;
import egate.digital.fasotour.model.*;
import egate.digital.fasotour.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.util.StringUtils;

@Service
@RequiredArgsConstructor
public class CircuitService {

    private final CircuitRepository         circuitRepository;
    private final GuideRepository           guideRepository;
    private final AgenceRepository          agenceRepository;
    private final SiteTouristiqueRepository siteRepository;

    @Transactional(readOnly = true)
    public List<CircuitResponseDTO> getAll() {
        return circuitRepository.findAll()
                .stream()
                .map(CircuitMapper::toResponseDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public CircuitResponseDTO getById(Long id) {
        return circuitRepository.findById(id)
                .map(CircuitMapper::toResponseDTO)
                .orElseThrow(() -> new RuntimeException("Circuit non trouvé : " + id));
    }

    @Transactional(readOnly = true)
    public CircuitResponseDTO getByCircuit(String circuit) {
        return circuitRepository.findByCircuitName(circuit)
                .map(CircuitMapper::toResponseDTO)
                .orElseThrow(() -> new RuntimeException("Circuit non trouvé : " + circuit));
    }

    // Plus récent en premier
    @Transactional(readOnly = true)
    public List<CircuitResponseDTO> getCircuitsOrderByCreatedAtDesc() {
        return circuitRepository.findAllOrderByCreatedAtDesc()
                .stream().map(CircuitMapper::toResponseDTO).collect(Collectors.toList());
    }

    // Plus ancien en premier
    @Transactional(readOnly = true)
    public List<CircuitResponseDTO> getCircuitsOrderByCreatedAtAsc() {
        return circuitRepository.findAllOrderByCreatedAtAsc()
                .stream().map(CircuitMapper::toResponseDTO).collect(Collectors.toList());
    }

    // Alphabétique A → Z
    @Transactional(readOnly = true)
    public List<CircuitResponseDTO> getCircuitsOrderByNameAsc() {
        return circuitRepository.findAllOrderByNameAsc()
                .stream().map(CircuitMapper::toResponseDTO).collect(Collectors.toList());
    }

    // Alphabétique Z → A
    @Transactional(readOnly = true)
    public List<CircuitResponseDTO> getCircuitsOrderByNameDesc() {
        return circuitRepository.findAllOrderByNameDesc()
                .stream().map(CircuitMapper::toResponseDTO).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public Page<CircuitResponseDTO> findPage(Pageable pageable) {
        return circuitRepository.findAll(pageable)
                .map(CircuitMapper::toResponseDTO);
    }

    // Search by name
    @Transactional(readOnly = true)
    public List<CircuitResponseDTO> searchByName(String name) {
        List<Circuit> circuits = (name == null || name.trim().isEmpty())
                ? circuitRepository.findAll()
                : circuitRepository.searchByName(name.trim());

        return circuits.stream()
                .map(CircuitMapper::toResponseDTO)
                .collect(Collectors.toList());
    }

    /*
    @Transactional
    public CircuitResponseDTO createCircuit(CircuitRequestDTO dto) {

        List<Guide> guides = guideRepository.findAllById(dto.guideIds());
        if (guides.isEmpty()) {
            throw new RuntimeException("Aucun guide trouvé pour les IDs : " + dto.guideIds());
        }

        Agence agence = agenceRepository.findById(dto.agenceId())
                .orElseThrow(() -> new RuntimeException("Agence non trouvée : " + dto.agenceId()));

        Set<SiteTouristique> sites = siteRepository.findAllById(dto.siteIds())
                .stream().collect(Collectors.toSet());

        if (circuitRepository.existsByCircuitName(dto.circuitName())){
            throw new EntityNotFoundException("Ce circuit existe déja! ");
        }

        Circuit circuit = CircuitMapper.toEntity(dto, guides, agence, sites);
        return CircuitMapper.toResponseDTO(circuitRepository.save(circuit));
    }
    */



    @Transactional
    public CircuitResponseDTO createCircuit(CircuitRequestDTO dto) {

        validerCircuit(dto);

        List<Guide> guides = guideRepository.findAllById(dto.guideIds());
        if (guides.isEmpty())
            throw new IllegalArgumentException(
                    "Aucun guide trouvé pour les IDs : " + dto.guideIds());

        Agence agence = agenceRepository.findById(dto.agenceId())
                .orElseThrow(() -> new IllegalArgumentException(
                        "Agence non trouvée : " + dto.agenceId()));

        Set<SiteTouristique> sites = siteRepository.findAllById(dto.siteIds())
                .stream().collect(Collectors.toSet());

        Circuit circuit = CircuitMapper.toEntity(dto, guides, agence, sites);
        return CircuitMapper.toResponseDTO(circuitRepository.save(circuit));
    }


    @Transactional
    public List<CircuitResponseDTO> createBatch(List<CircuitRequestDTO> dtos) {
        return dtos.stream()
                .map(this::createCircuit)
                .collect(Collectors.toList());
    }

    /*
    @Transactional
    public CircuitResponseDTO updatcircuite(Long id, CircuitRequestDTO dto) {

        Circuit circuit = circuitRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Circuit non trouvé : " + id));

        circuit.setCircuitName(dto.circuitName());
        circuit.setDateDebut(dto.dateDebut());
        circuit.setDateFin(dto.dateFin());
        circuit.setDescription(dto.description());
        circuit.setDuree(dto.duree());
        circuit.setPrixIndividuel(dto.prixIndividuel());
        circuit.setNombreExact(dto.nombreExact());
        circuit.setStatut(dto.statut());
        circuit.setTransport(dto.transport());

        if (dto.guideIds() != null && !dto.guideIds().isEmpty()) {
            circuit.setGuides(guideRepository.findAllById(dto.guideIds()));
        }
        if (dto.agenceId() != null) {
            Agence agence = agenceRepository.findById(dto.agenceId())
                    .orElseThrow(() -> new RuntimeException("Agence non trouvée : " + dto.agenceId()));
            circuit.setAgence(agence);
        }
        if (dto.siteIds() != null && !dto.siteIds().isEmpty()) {
            Set<SiteTouristique> sites = siteRepository.findAllById(dto.siteIds())
                    .stream().collect(Collectors.toSet());
            circuit.setSites(sites);
        }

        return CircuitMapper.toResponseDTO(circuitRepository.save(circuit));
    }
     */

    @Transactional
    public CircuitResponseDTO updatcircuite(Long id, CircuitRequestDTO dto) {

        Circuit circuit = circuitRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Circuit non trouvé : " + id));

        circuit.setCircuitName(dto.circuitName());
        circuit.setDescription(dto.description());
        circuit.setDateDebut(dto.dateDebut());
        circuit.setDateFin(dto.dateFin());
        circuit.setDateLimiteReservation(dto.dateLimiteReservation());
        circuit.setLieuRassemblement(dto.lieuRassemblement());
        circuit.setHeureDepart(dto.heureDepart());
        circuit.setImage(dto.image());
        circuit.setPrixIndividuel(dto.prixIndividuel());
        circuit.setNombreExact(dto.nombreExact());
        circuit.setStatut(dto.statut());
        circuit.setTransport(dto.transport());


        if (dto.dateDebut() != null && dto.dateFin() != null) {
            long diffJours = ChronoUnit.DAYS.between(dto.dateDebut(), dto.dateFin());
            circuit.setDuree(Duration.ofDays(diffJours));
        }

        if (dto.guideIds() != null && !dto.guideIds().isEmpty()) {
            circuit.setGuides(guideRepository.findAllById(dto.guideIds()));
        }

        if (dto.agenceId() != null) {
            Agence agence = agenceRepository.findById(dto.agenceId())
                    .orElseThrow(() -> new RuntimeException("Agence non trouvée : " + dto.agenceId()));
            circuit.setAgence(agence);
        }

        if (dto.siteIds() != null && !dto.siteIds().isEmpty()) {
            Set<SiteTouristique> sites = siteRepository.findAllById(dto.siteIds())
                    .stream().collect(Collectors.toSet());
            circuit.setSites(sites);
        }

        return CircuitMapper.toResponseDTO(circuitRepository.save(circuit));
    }
    @Transactional
    public void remov(Long id) {
        if (!circuitRepository.existsById(id)) {
            throw new RuntimeException("Circuit non trouvé : " + id);
        }
        circuitRepository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public List<CircuitResponseDTO> getByAgence(Long agenceId) {
        return circuitRepository.findByAgenceId(agenceId)
                .stream().map(CircuitMapper::toResponseDTO)
                .collect(Collectors.toList());
    }


    // Validatio

    private void validerCircuit(CircuitRequestDTO dto) {

        // ── Champs texte obligatoires ────────────────────────────────────────
        Map<String, String> textFields = new LinkedHashMap<>();
        textFields.put("circuitName",  dto.circuitName());
        textFields.put("statut",       dto.statut());

        textFields.forEach((champ, valeur) -> {
            if (!StringUtils.hasText(valeur))
                throw new IllegalArgumentException(
                        "Le champ « " + champ + " » ne peut pas être vide !");
        });


        // ── Dates debut obligatoires ───────────────────────────────────────────────

        LocalDate today = LocalDate.now();
        if (dto.dateDebut() == null || dto.dateDebut().isBefore(today))
            throw new IllegalArgumentException("La date de début est obligatoire et doit être supérieure ou égale à aujourd'hui");


        // ── obligatoires et Cohérence de dates de Fin ─────────────────────────────────────────────
        if (dto.dateFin() == null || !dto.dateFin().isAfter(dto.dateDebut()))
            throw new IllegalArgumentException("La date de fin est obligatoire et doit être supérieure à la date de début.");


        if (dto.dateLimiteReservation() == null || !dto.dateLimiteReservation().isBefore(dto.dateDebut()))
            throw new IllegalArgumentException("La date limite de réservation est obligatoire et doit être inférieure à la date de début.");


        // ── Prix ─────────────────────────────────────────────────────────────
        if (dto.prixIndividuel() == null || dto.prixIndividuel() < 0)
            throw new IllegalArgumentException("Le prix est obligatoire et ne peut pas être négatif.");

        // ── Sites ─────────────────────────────────────────────────────────────
        if (dto.siteIds() == null || dto.siteIds().isEmpty())
            throw new IllegalArgumentException("Veuillez sélectionner au moins 2 sites.");

        if (dto.siteIds().size() < 2)
            throw new IllegalArgumentException("Le circuit doit contenir au moins 2 sites.");

        // ── Unicité du nom ───────────────────────────────────────────────────
        if (circuitRepository.existsByCircuitName(dto.circuitName()))
            throw new IllegalStateException("Un circuit avec ce nom existe déjà !");
    }
    @Transactional(readOnly = true)
    public List<CircuitResponseDTO> getByStatut(String statut) {
        return circuitRepository.findByStatut(statut)
                .stream().map(CircuitMapper::toResponseDTO)
                .collect(Collectors.toList());
    }
}