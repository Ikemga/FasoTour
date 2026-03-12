package egate.digital.fasotour.mappers;

import java.util.Set;
import java.util.stream.Collectors;

import egate.digital.fasotour.dto.user.TouristeResponseDTO;
import egate.digital.fasotour.model.Langue;
import egate.digital.fasotour.model.Role;
import egate.digital.fasotour.model.Touriste;

public class TouristeMapper {

    public static TouristeResponseDTO toDTO(Touriste touriste) {

        return new TouristeResponseDTO(
                touriste.getId(),
                touriste.getNomComplet(),
                touriste.getMail(),
                touriste.getTelephone(),
                touriste.getPreferenceTouristique(),
                touriste.getActif(),
                touriste.getCreateAt(),
                touriste.getPays(),

/*
                touriste.getLangues()
                        .stream()
                        .map(Langue::getLangues)
                        .collect(Collectors.toSet()),
*/
                touriste.getLangues() == null ? Set.of() :
                        touriste.getLangues().stream()
                                .map(Langue::getLangues)
                                .collect(Collectors.toSet()),

                touriste.getRoles()
                        .stream()
                        .map(Role::getRoles)
                        .collect(Collectors.toSet())
        );
    }
}