package egate.digital.fasotour.mappers;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import egate.digital.fasotour.dto.site.*;
import egate.digital.fasotour.model.*;

public class SiteTouristiqueMapper {

    private SiteTouristiqueMapper() {}

    public static SiteTouristiqueResponseDTO toResponseDTO(SiteTouristique site) {

        if (site == null) return null;

        List<CategorieResponseDTO> categoriesDTO = null;

        if (site.getCategories() != null) {
            categoriesDTO = new ArrayList<>(site.getCategories())
                    .stream()
                    .map(cat -> new CategorieResponseDTO(
                            cat.getId(),
                            cat.getCategorie(),
                            cat.getDescription(),
                            cat.getStatut()
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
                site.getCreateedAt(),
                site.getLocalisation(),
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
        return site;
    }
}