package egate.digital.fasotour.mappers;

import egate.digital.fasotour.dto.UtilisateurDTO;
import egate.digital.fasotour.model.Role;
import egate.digital.fasotour.model.Utilisateur;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
public class UtilisateurMapper {

    public UtilisateurDTO toDTO(Utilisateur u) {
        UtilisateurDTO dto = new UtilisateurDTO();
        dto.setId(u.getId());
        dto.setNomComplet(u.getNomComplet());
        dto.setMail(u.getMail());
        dto.setTelephone(u.getTelephone());
        dto.setAdresse(u.getAdresse());
        dto.setPays(u.getPays());
        dto.setPhoto(u.getPhoto());
        dto.setBio(u.getBio());
        dto.setActif(u.getActif());
        dto.setCreateAt(u.getCreateAt());
        dto.setRoles(
                u.getRoles().stream()
                        .map(Role::getRoles)
                        .collect(Collectors.toSet())
        );
        return dto;
    }
}