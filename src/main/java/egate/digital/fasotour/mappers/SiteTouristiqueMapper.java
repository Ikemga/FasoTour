package egate.digital.fasotour.mappers;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import egate.digital.fasotour.dto.site.*;
import egate.digital.fasotour.model.*;

public class SiteTouristiqueMapper {

    private SiteTouristiqueMapper() {}

    public static SiteTouristiqueResponseDTO toResponseDTO(SiteTouristique site) {

        if (site == null) return null;

        List<CategorieSimpledDTO> categoriesDTO = null;

        if (site.getCategories() != null) {
            categoriesDTO = new ArrayList<>(site.getCategories())
                    .stream()
                    .map(cat -> new CategorieSimpledDTO(
                            cat.getId(),
                            cat.getCategorie()
                    ))
                    .collect(Collectors.toList());
        }

        return new SiteTouristiqueResponseDTO(
                site.getId(),
                site.getNom(),
                site.getDescription(),
                site.getRegion(),
                site.getImage(),
                site.getNoteMoyenne(),
                site.getVideo(),
                site.getFichier(),
                site.getCreatedAt(),
                site.getLocalisation(),
                //Add Attribut
                site.getHeureOuverture(),
                site.getHeureFermeture(),
                site.getTarif(),
                site.getStatut(),

                categoriesDTO
        );
    }

    public static SiteTouristique toEntity(SiteTouristiqueRequestDTO dto) {
        if (dto == null) return null;

        SiteTouristique site = new SiteTouristique();
        site.setNom(dto.nom());
        site.setDescription(dto.description());
        site.setRegion(dto.region());
        site.setImage(dto.image());
        site.setNoteMoyenne(dto.noteMoyenne());
        site.setVideo(dto.video());
        site.setFichier(dto.fichier());
        site.setLocalisation(dto.localisation());

        //Add Attribut

        site.setHeureOuverture(dto.heureOuverture());
        site.setHeureOuverture(dto.heureFermeture());
        site.setTarif(dto.tarif());
        site.setStatut(dto.statut());

        return site;
    }
}