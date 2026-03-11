package egate.digital.fasotour.dto.site;

import java.time.LocalTime;
import java.util.List;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record SiteTouristiqueRequestDTO(

        @NotBlank(message="Veuillez entrer le nom du site touristque")
    String nom,
        @Size(min=10, max=200)
        String description,
        String region,
        String image,
        String noteMoyenne,
        String video,
        String fichier,

        // Add Attribut
        LocalTime horaire,
        Double tarif,
        String statut,
        @NotBlank(message="Veuillez entrer la localisation")
        String localisation,

        List<Long> categorieIds

) {

}
