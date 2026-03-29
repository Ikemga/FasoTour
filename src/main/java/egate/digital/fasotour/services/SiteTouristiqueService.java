package egate.digital.fasotour.services;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import egate.digital.fasotour.dto.site.SiteTouristiqueRequestDTO;
import egate.digital.fasotour.dto.site.SiteTouristiqueResponseDTO;
import egate.digital.fasotour.mappers.SiteTouristiqueMapper;
import egate.digital.fasotour.model.Categorie;
import egate.digital.fasotour.model.SiteTouristique;
import egate.digital.fasotour.repository.CategorieRepository;
import egate.digital.fasotour.repository.SiteTouristiqueRepository;
import org.springframework.util.StringUtils;

import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class SiteTouristiqueService {

    private final SiteTouristiqueRepository siteTouristiqueRepository;
    private final CategorieRepository categorieRepository;

    public SiteTouristiqueService(
            SiteTouristiqueRepository siteTouristiqueRepository,
            CategorieRepository categorieRepository) {
        this.siteTouristiqueRepository = siteTouristiqueRepository;
        this.categorieRepository = categorieRepository;
    }

    public Page<SiteTouristiqueResponseDTO> findAll(Pageable pageable) {
        return siteTouristiqueRepository.findAll(pageable)
                .map(SiteTouristiqueMapper::toResponseDTO);
    }

    public List<SiteTouristiqueResponseDTO> getAllSitesNewestFirst() {
        return siteTouristiqueRepository.findAllByOrderByCreatedAtDesc()
                .stream()
                .map(SiteTouristiqueMapper::toResponseDTO)
                .toList();
    }
        // searc
    public List<SiteTouristiqueResponseDTO> search(String q) {
        if (q == null || q.isBlank()) return getAllSitesNewestFirst();
        return siteTouristiqueRepository.search(q.trim())
                .stream()
                .map(SiteTouristiqueMapper::toResponseDTO)
                .toList();
    }

    public SiteTouristiqueResponseDTO findById(Long id) {
        return siteTouristiqueRepository.findById(id)
                .map(SiteTouristiqueMapper::toResponseDTO)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Site touristique introuvable avec l'id : " + id));
    }

    public List<SiteTouristiqueResponseDTO> findByRegion(String region) {
        return siteTouristiqueRepository.findByRegionIgnoreCase(region)
                .stream()
                .map(SiteTouristiqueMapper::toResponseDTO)
                .collect(Collectors.toList());
    }

    public List<SiteTouristiqueResponseDTO> getAllAlphabetical() {
        return siteTouristiqueRepository.findAllOrderByNomAsc()
                .stream()
                .map(SiteTouristiqueMapper::toResponseDTO)
                .collect(Collectors.toList());
    }

    @Transactional
    public SiteTouristiqueResponseDTO create(SiteTouristiqueRequestDTO dto) {
        validerSiteTouristique(dto);

        SiteTouristique site = SiteTouristiqueMapper.toEntity(dto);
        site.setCategories(resolveCategories(dto.categorieIds()));
        return SiteTouristiqueMapper.toResponseDTO(siteTouristiqueRepository.save(site));
    }

    @Transactional
    public SiteTouristiqueResponseDTO update(Long id, SiteTouristiqueRequestDTO dto) {
        SiteTouristique site = siteTouristiqueRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Site touristique introuvable avec l'id : " + id));

        site.setNom(dto.nom());
        site.setDescription(dto.description());
        site.setRegion(dto.region());
        site.setImage(dto.image());
        site.setNoteMoyenne(dto.noteMoyenne());
        site.setVideo(dto.video());
        site.setFichier(dto.fichier());
        site.setLocalisation(dto.localisation());
        site.setCategories(resolveCategories(dto.categorieIds()));

        site.setHeureOuverture(dto.heureOuverture());
        site.setHeureFermeture(dto.heureFermeture());

        site.setTarif(dto.tarif());
        site.setStatut(dto.statut());

        return SiteTouristiqueMapper.toResponseDTO(siteTouristiqueRepository.save(site));
    }
    @Transactional
    public void delete(Long id) {
        if (!siteTouristiqueRepository.existsById(id)) {
            throw new EntityNotFoundException(
                    "Site touristique introuvable avec l'id : " + id);
        }
        siteTouristiqueRepository.deleteById(id);
    }


    private void validerSiteTouristique(SiteTouristiqueRequestDTO dto) {
        Map<String, String> textFields = new LinkedHashMap<>();
        textFields.put("nom",          dto.nom());
        textFields.put("localisation", dto.localisation());
        textFields.put("statut",       dto.statut());

        textFields.forEach((champ, valeur) -> {
            if (!StringUtils.hasText(valeur))
                throw new IllegalArgumentException(
                        "Le champ " + champ + " ne peut pas être vide !");
        });

        if (dto.heureOuverture() == null || dto.heureFermeture() == null)
            throw new IllegalArgumentException("Les heures d'ouverture  et de fermeture ne peuvent pas être vides !");

        if (siteTouristiqueRepository.existsByNom(dto.nom()))
            throw new IllegalStateException("Un site avec ce nom existe déjà !");
    }



    private Set<Categorie> resolveCategories(List<Long> ids) {
        if (ids == null || ids.isEmpty()) return Collections.emptySet();
        List<Categorie> categories = categorieRepository.findAllById(ids);
        if (categories.size() != ids.size()) {
            throw new EntityNotFoundException(
                    "Une ou plusieurs catégories sont introuvables.");
        }
        return new HashSet<>(categories);
    }
}