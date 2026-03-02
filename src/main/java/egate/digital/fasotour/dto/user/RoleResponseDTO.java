package egate.digital.fasotour.dto.user;

import java.util.Set;

public record RoleResponseDTO(

        Long id,
        String role,
        String description,
        String statut,
        Set<Long> utilisateurIds

) {}