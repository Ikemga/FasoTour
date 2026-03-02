package egate.digital.fasotour.mappers;

import egate.digital.fasotour.dto.others.AvisRequestDTO;
import egate.digital.fasotour.dto.others.AvisResponseDTO;
import egate.digital.fasotour.model.*;

public class AvisMapper {

    private AvisMapper() {}

    /**
     * dto to etity
    */
    public static Avis toEntity(
            AvisRequestDTO dto,
            Circuit circuit,
            Guide guide,
            Touriste touriste
    ) {

        Avis avis = new Avis();

        avis.setAvis(dto.avis());
        avis.setNote(dto.note());
        avis.setDate(dto.date());

        avis.setCircuit(circuit);
        avis.setGuide(guide);
        avis.setTouriste(touriste);

        return avis;
    }

    /**
     * Etity to dto
    */
    public static AvisResponseDTO toDTO(Avis avis) {

        Long circuitId = null;
        String circuitNom = null;

        Long guideId = null;
        String guideNom = null;

        Long touristeId = null;
        String touristeNom = null;

        if (avis.getCircuit() != null) {
            circuitId = avis.getCircuit().getId();
            circuitNom = avis.getCircuit().getCircuitName(); // adapte si besoin
        }

        if (avis.getGuide() != null) {
            guideId = avis.getGuide().getId();
            guideNom = avis.getGuide().getNomComplet();
        }

        if (avis.getTouriste() != null) {
            touristeId = avis.getTouriste().getId();
            touristeNom = avis.getTouriste().getNomComplet();
        }

        return new AvisResponseDTO(
                avis.getId(),
                avis.getAvis(),
                avis.getNote(),
                avis.getDate(),
                circuitId,
                circuitNom,
                guideId,
                guideNom,
                touristeId,
                touristeNom
        );
    }
}