package egate.digital.fasotour.dto.site;

import jakarta.validation.constraints.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;

public record CircuitRequestDTO(

        @NotBlank(message = "Le nom du circuit est obligatoire.")
        String circuitName,

        String description,

        @NotNull(message = "La date de début est obligatoire.")
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

) {
        // ✅ Factory method pour construire depuis multipart/form-data
        public static CircuitRequestDTO fromParts(
                String circuitName,
                String description,
                String dateDebut,
                String dateFin,
                String dateLimiteReservation,
                String lieuRassemblement,
                String heureDepart,
                String prixIndividuel,
                String nombreExact,
                String statut,
                String transport,
                String agenceId,
                List<String> siteIds,
                List<String> guideIds,
                String imageUrl
        ) {
                return new CircuitRequestDTO(
                        circuitName,
                        description,
                        LocalDate.parse(dateDebut),
                        LocalDate.parse(dateFin),
                        LocalDate.parse(dateLimiteReservation),
                        Double.parseDouble(prixIndividuel),
                        Integer.parseInt(nombreExact),
                        statut,
                        Boolean.parseBoolean(transport),
                        lieuRassemblement,
                        (heureDepart != null && !heureDepart.isBlank()) ? LocalTime.parse(heureDepart) : null,
                        imageUrl,
                        Long.parseLong(agenceId),
                        siteIds == null ? List.of() : siteIds.stream().map(Long::parseLong).collect(Collectors.toList()),
                        guideIds == null ? List.of() : guideIds.stream().map(Long::parseLong).collect(Collectors.toList())
                );
        }
}