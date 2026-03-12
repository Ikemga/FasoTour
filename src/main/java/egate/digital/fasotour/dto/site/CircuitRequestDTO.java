package egate.digital.fasotour.dto.site;

import jakarta.validation.constraints.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public record CircuitRequestDTO(

        @NotBlank(message = "Le nom du circuit est obligatoire.")
        String circuitName,

        @NotBlank(message = "La description est obligatoire.")
        String description,

        @NotNull(message = "La date de début est obligatoire.")
        @FutureOrPresent(message = "La date de début doit être supérieure ou égale à aujourd'hui.")
        LocalDate dateDebut,

        @NotNull(message = "La date de fin est obligatoire.")
        LocalDate dateFin,

        @NotNull(message = "La date limite de réservation est obligatoire.")
        LocalDate dateLimiteReservation,

        @NotNull(message = "Le prix est obligatoire.")
        @PositiveOrZero(message = "Le prix ne peut pas être négatif.")
        Double prixIndividuel,

        Integer nombreExact,
        String statut,
        Boolean transport,
        String lieuRassemblement,
        LocalTime heureDepart,
        String image,


        Long agenceId,

        @NotEmpty(message = "Veuillez sélectionner au moins 2 sites.")
        @Size(min = 2, message = "Le circuit doit contenir au moins 2 sites.")
        List<Long> siteIds,

        List<Long> guideIds
) {}