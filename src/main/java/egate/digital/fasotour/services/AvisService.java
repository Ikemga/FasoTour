package egate.digital.fasotour.services;

import egate.digital.fasotour.dto.others.AvisRequestDTO;
import egate.digital.fasotour.dto.others.AvisResponseDTO;
import egate.digital.fasotour.mappers.AvisMapper;
import egate.digital.fasotour.model.Avis;
import egate.digital.fasotour.model.Circuit;
import egate.digital.fasotour.model.Guide;
import egate.digital.fasotour.model.Touriste;
import egate.digital.fasotour.repository.AvisRepository;
import egate.digital.fasotour.repository.CircuitRepository;
import egate.digital.fasotour.repository.GuideRepository;
import egate.digital.fasotour.repository.TouristeRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class AvisService {

    private final AvisRepository avisRepository;
    private final CircuitRepository circuitRepository;
    private final GuideRepository guideRepository;
    private final TouristeRepository touristeRepository;

    @Transactional
    public AvisResponseDTO createAvis(AvisRequestDTO dto) {

        Circuit circuit = circuitRepository.findById(dto.circuitId())
                .orElseThrow(() -> new EntityNotFoundException("Circuit non trouvé avec l'id : " + dto.circuitId()));

        Guide guide = guideRepository.findById(dto.guideId())
                .orElseThrow(() -> new EntityNotFoundException("Guide non trouvé avec l'id : " + dto.guideId()));

        Touriste touriste = touristeRepository.findById(dto.touristeId())
                .orElseThrow(() -> new EntityNotFoundException("Touriste non trouvé avec l'id : " + dto.touristeId()));

        Avis avis = AvisMapper.toEntity(dto, circuit, guide, touriste);
        Avis saved = avisRepository.save(avis);

        return AvisMapper.toDTO(saved);
    }


    @Transactional(readOnly = true)
    public List<AvisResponseDTO> getAllAvis() {
        return avisRepository.findAll()
                .stream()
                .map(AvisMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public AvisResponseDTO getAvisById(Long id) {
        Avis avis = avisRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Avis non trouvé avec l'id : " + id));
        return AvisMapper.toDTO(avis);
    }

    @Transactional(readOnly = true)
    public List<AvisResponseDTO> getAvisByCircuit(Long circuitId) {
        return avisRepository.findByCircuitId(circuitId)
                .stream()
                .map(AvisMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<AvisResponseDTO> getAvisByGuide(Long guideId) {
        return avisRepository.findByGuideId(guideId)
                .stream()
                .map(AvisMapper::toDTO)
                .collect(Collectors.toList());
    }


    public AvisResponseDTO updateAvis(Long id, AvisRequestDTO dto) {
        Avis avis = avisRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Avis non trouvé avec l'id : " + id));

        avis.setAvis(dto.avis());
        avis.setNote(dto.note());
        avis.setDate(dto.date());

        Avis updated = avisRepository.save(avis);
        return AvisMapper.toDTO(updated);
    }


    public void deleteAvis(Long id) {
        if (!avisRepository.existsById(id)) {
            throw new EntityNotFoundException("Avis non trouvé avec l'id : " + id);
        }
        avisRepository.deleteById(id);
    }
}