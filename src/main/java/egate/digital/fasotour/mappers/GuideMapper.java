package egate.digital.fasotour.mappers;

import java.util.Set;
import java.util.stream.Collectors;

import egate.digital.fasotour.dto.user.GuideRequestDTO;
import egate.digital.fasotour.dto.user.GuideResponseDTO;
import egate.digital.fasotour.model.Agence;
import egate.digital.fasotour.model.Guide;
import egate.digital.fasotour.model.Langue;
import egate.digital.fasotour.model.Role;

public class GuideMapper {

    /**
     * RequestDTO → Entity
     */
    public static Guide toEntity(
            GuideRequestDTO dto,
            Set<Langue> langues,
            Set<Role> roles,
            Set<Agence> agences
    ) {

        Guide guide = new Guide();

        guide.setNomComplet(dto.nomComplet());
        guide.setAdresse(dto.adresse());
        guide.setMail(dto.mail());
        guide.setTelephone(dto.telephone());
        guide.setMotDePasse(dto.motDePasse());
        guide.setPays(dto.pays());
        guide.setPhoto(dto.photo());
        guide.setBio(dto.bio());
        guide.setExperience(dto.experience());
        guide.setPreferenceTouristique(dto.preferenceTouristique());

        guide.setLangues(langues);
        guide.setRoles(roles);
        guide.setAgences(agences);

        return guide;
    }



    /**
     * Entity → ResponseDTO
     */
    public static GuideResponseDTO toDTO(Guide guide) {

        Set<String> langueNames = guide.getLangues()
                .stream()
                .map(Langue::getLangues)
                .collect(Collectors.toSet());

        return new GuideResponseDTO(
                guide.getId(),
                guide.getNomComplet(),
                guide.getMail(),
                guide.getExperience(),
                guide.getPreferenceTouristique(),
                langueNames
        );
    }
}