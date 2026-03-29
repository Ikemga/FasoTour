package egate.digital.fasotour.mappers;

import java.util.HashSet;
import java.util.Set;

import egate.digital.fasotour.dto.user.AgenceRequestDTO;
import egate.digital.fasotour.dto.user.AgenceResponseDTO;
import egate.digital.fasotour.model.Agence;
import egate.digital.fasotour.model.Guide;
import egate.digital.fasotour.model.Langue;
import egate.digital.fasotour.model.Role;

public class AgenceMapper {

    private AgenceMapper() {}

    // DTO → Entité
    public static Agence toEntity(
            AgenceRequestDTO dto,
            Set<Langue> langues,
            Set<Role> roles,
            Set<Guide> guides
    ) {
        Agence agence = new Agence();

        agence.setNomComplet(dto.nomComplet());
        agence.setAdresse(dto.adresse());
        agence.setMail(dto.mail());
        agence.setTelephone(dto.telephone());
        agence.setMotDePasse(dto.motDePasse());
        agence.setPays(dto.pays());
        agence.setPhoto(dto.photo());
        agence.setBio(dto.bio());
        agence.setSitWeb(dto.sitWeb());
        agence.setPageFacebook(dto.pageFacebook());
        agence.setNumeroAgrement(dto.numeroAgrement());
        agence.setDateCreation(dto.dateCreation());
        agence.setLangues(langues);
        agence.setRoles(new HashSet<>(roles));
        agence.setGuides(guides);

        return agence;
    }

    // Entité → DTO
    public static AgenceResponseDTO toDTO(Agence agence) {
        return new AgenceResponseDTO(
                agence.getId(),
                agence.getNomComplet(),
                agence.getMail(),
                agence.getTelephone(),
                agence.getActif(),
                agence.getCreateAt(),
                agence.getSitWeb(),
                agence.getPageFacebook(),
                agence.getNumeroAgrement(),
                agence.getDateCreation()
        );
    }
}