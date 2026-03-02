package egate.digital.fasotour.mappers;

import java.util.Collections;
import java.util.Set;
import java.util.stream.Collectors;

import egate.digital.fasotour.dto.user.RoleRequestDTO;
import egate.digital.fasotour.dto.user.RoleResponseDTO;
import egate.digital.fasotour.model.Role;

public class RoleMapper {

    private RoleMapper() {}

    /**
     * DTO → Entity
     * On n'injecte pas les utilisateurs ici,
     */
    public static Role toEntity(RoleRequestDTO dto) {
        if (dto == null) throw new IllegalArgumentException("RoleRequestDTO ne peut pas être null");

        Role role = new Role();
        role.setRoles(dto.role());
        role.setDescription(dto.description());
        role.setStatut(dto.statut());
        return role;
    }

    /**
     * Entity → DTO
     */
    public static RoleResponseDTO toDTO(Role role) {
        if (role == null) return null;

        // Récupèrat les IDs des utilisateurs associés
        Set<Long> utilisateurIds = (role.getUtilisateurs() != null)
                ? role.getUtilisateurs()
                    .stream()
                    .map(u -> u.getId())
                    .collect(Collectors.toSet())
                : Collections.emptySet();

        return new RoleResponseDTO(
                role.getId(),
                role.getRoles(),
                role.getDescription(),
                role.getStatut(),
                utilisateurIds
        );
    }
}