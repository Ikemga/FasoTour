package egate.digital.fasotour.dto.site;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.time.LocalTime;
import java.util.List;

public record SiteTouristiqueRequestDTO(

        @NotBlank(message="Veuillez entrer le nom du site touristique")
        String nom,

        @Size(min=10, max=200)
        String description,

        String region,
        String image,
        Double noteMoyenne,
        String video,
        String fichier,
        LocalTime heureOuverture,
        LocalTime heureFermeture,
        Double tarif,
        String statut,

        @NotBlank(message="Veuillez entrer la localisation")
        String localisation,

        List<Long> categorieIds

) {}