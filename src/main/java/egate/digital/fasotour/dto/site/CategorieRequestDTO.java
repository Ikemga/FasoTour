package egate.digital.fasotour.dto.site;

import jakarta.validation.constraints.NotBlank;

public record CategorieRequestDTO(

    @NotBlank(message="Veuillez entrer nom de la catégorie")
    String categorie,
    String description,
    String statut
) {}