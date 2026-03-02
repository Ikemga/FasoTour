package egate.digital.fasotour.dto.user;

import java.util.Set;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record TouristeRequestDTO(

        String nomComplet,
        String adresse,
        @NotBlank(message = "Le mail ne peut pas être null")
        String mail,
        @Size(min = 8)
        String telephone,
        @Size(min = 8, max = 50)
        String motDePasse,
        String pays,
        String bio,
        Set<Long> langueIds,
        Set<Long> roleIds,
        String preferenceTouristique

) {}