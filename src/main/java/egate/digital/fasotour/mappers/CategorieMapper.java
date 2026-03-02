package egate.digital.fasotour.mappers;

import egate.digital.fasotour.dto.site.CategorieRequestDTO;
import egate.digital.fasotour.dto.site.CategorieResponseDTO;
import egate.digital.fasotour.model.Categorie;

public class CategorieMapper {

    private CategorieMapper() {}

    /**
     * Response entity entity to dto
    */
    public static CategorieResponseDTO toResponseDTO(Categorie categorie) {

        if (categorie == null) return null;

        return new CategorieResponseDTO(
                categorie.getId(),
                categorie.getCategorie(),
                categorie.getDescription(),
                categorie.getStatut()
        );
    }

    /**
     * resquet entity  dto to entity
    */
    public static Categorie toEntity(CategorieRequestDTO dto) {

        if (dto == null) return null;

        Categorie categorie = new Categorie();

        categorie.setCategorie(dto.categorie());
        categorie.setDescription(dto.description());
        categorie.setStatut(dto.statut());

        return categorie;
    }
}