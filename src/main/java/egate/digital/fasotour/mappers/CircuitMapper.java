package egate.digital.fasotour.mappers;

import java.time.Duration;
import java.time.temporal.Temporal;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import egate.digital.fasotour.dto.site.*;
import egate.digital.fasotour.dto.user.AgenceSimpleDTO;
import egate.digital.fasotour.dto.user.GuideSimpleDTO;
import egate.digital.fasotour.model.*;

public class CircuitMapper {

    private CircuitMapper() {}


    public static CircuitResponseDTO toResponseDTO(Circuit circuit) {
        if (circuit == null) return null;

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

        return new CircuitResponseDTO(
                circuit.getId(),
                circuit.getCircuitName(),
                circuit.getDateDebut(),
                circuit.getDateFin(),
                circuit.getDescription(),
                circuit.getDuree(),
                circuit.getPrixIndividuel(),
                circuit.getNombreMini(),
                circuit.getNombreMax(),
                circuit.getNombreExact(),
                circuit.getCreatedAt(),
                circuit.getStatut(),
                circuit.isTransport(),
                // Add
                circuit.getLieuRassemblement(),
                circuit.getHeureDepart(),
                circuit.getDateLimiteReservation(),

                guideDTOs,
                agenceDTO,
                siteDTOs
        );
    }


    public static Circuit toEntity(
            CircuitRequestDTO dto,
            List<Guide> guides,
            Agence agence,
            Set<SiteTouristique> sites
    ) {
        if (dto == null) return null;

        Circuit circuit = new Circuit();
        circuit.setCircuitName(dto.circuitName());
        circuit.setDateDebut(dto.dateDebut());
        circuit.setDateFin(dto.dateFin());
        circuit.setDescription(dto.description());
        circuit.setDuree(Duration.between((Temporal) dto.dateDebut(), (Temporal) dto.dateFin()));
        circuit.setPrixIndividuel(dto.prixIndividuel());
        circuit.setNombreMini(dto.nombreMini());
        circuit.setNombreMax(dto.nombreMax());
        circuit.setNombreExact(dto.nombreExact());
        circuit.setStatut(dto.statut());
        circuit.setTransport(dto.transport());

        circuit.setGuides(guides);
        circuit.setAgence(agence);
        circuit.setSites(sites);

        return circuit;
    }
}