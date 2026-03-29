package egate.digital.fasotour.mappers;

import java.time.temporal.ChronoUnit;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import egate.digital.fasotour.dto.site.*;
import egate.digital.fasotour.dto.user.AgenceSimpleDTO;
import egate.digital.fasotour.dto.user.GuideSimpleDTO;
import egate.digital.fasotour.model.*;

public class CircuitMapper {

    private CircuitMapper() {}

    // ── Entity ResponseDTO ───────────────────────────────────────────────
    public static CircuitResponseDTO toResponseDTO(Circuit circuit) {
        if (circuit == null) return null;

        // Guides
        List<GuideSimpleDTO> guideDTOs = Collections.emptyList();
        if (circuit.getGuides() != null) {
            guideDTOs = circuit.getGuides()
                    .stream()
                    .map(g -> new GuideSimpleDTO(g.getId(), g.getNomComplet()))
                    .collect(Collectors.toList());
        }

        // Agence
        AgenceSimpleDTO agenceDTO = null;
        if (circuit.getAgence() != null) {
            agenceDTO = new AgenceSimpleDTO(
                    circuit.getAgence().getId(),
                    circuit.getAgence().getNomComplet()
            );
        }

        // Sites
        List<SiteSimpleDTO> siteDTOs = Collections.emptyList();
        if (circuit.getSites() != null) {
            siteDTOs = circuit.getSites()
                    .stream()
                    .map(s -> new SiteSimpleDTO(s.getId(), s.getNom()))
                    .collect(Collectors.toList());
        }

        Long duree = circuit.getDuree();

        return new CircuitResponseDTO(
                circuit.getId(),
                circuit.getCircuitName(),
                circuit.getDateDebut(),
                circuit.getDateFin(),
                circuit.getDescription(),
                duree,
                circuit.getPrixIndividuel(),
                circuit.getNombreExact(),
                circuit.getNombreRestant(),
                circuit.getCreatedAt(),
                circuit.getStatut(),
                circuit.isTransport(),
                circuit.getLieuRassemblement(),
                circuit.getHeureDepart(),
                circuit.getDateLimiteReservation(),
                circuit.getImage(),
                guideDTOs,
                agenceDTO,
                siteDTOs
        );
    }

    // ── RequestDTO  Entity ────────────────────────────────────────────────
    public static Circuit toEntity(
            CircuitRequestDTO dto,
            List<Guide> guides,
            Agence agence,
            Set<SiteTouristique> sites
    ) {
        if (dto == null) return null;

        Circuit circuit = new Circuit();

        circuit.setCircuitName(dto.circuitName());
        circuit.setDescription(dto.description());
        circuit.setDateDebut(dto.dateDebut());
        circuit.setDateFin(dto.dateFin());
        circuit.setDateLimiteReservation(dto.dateLimiteReservation());
        circuit.setLieuRassemblement(dto.lieuRassemblement());
        circuit.setHeureDepart(dto.heureDepart());
        circuit.setImage(dto.image());

        if (dto.dateDebut() != null && dto.dateFin() != null) {
            long diffJours = ChronoUnit.DAYS.between(dto.dateDebut(), dto.dateFin());
            circuit.setDuree(diffJours);
        }

        circuit.setPrixIndividuel(dto.prixIndividuel());
        circuit.setNombreExact(dto.nombreExact());
        circuit.setNombreRestant(dto.nombreExact());
        circuit.setStatut(dto.statut());
        circuit.setTransport(dto.transport());

        circuit.setGuides(new HashSet<>(guides));
        circuit.setAgence(agence);
        circuit.setSites(sites);

        return circuit;
    }
}