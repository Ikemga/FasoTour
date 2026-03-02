package egate.digital.fasotour.dto.user;

public record RoleRequestDTO(

        String role,
        String description,
        String statut,
        Long utilisateurId

) {}