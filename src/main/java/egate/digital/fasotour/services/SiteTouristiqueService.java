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

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
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

    @Transactional
    public SiteTouristiqueResponseDTO create(SiteTouristiqueRequestDTO dto) {
        if (siteTouristiqueRepository.existsByNom(dto.nom())) {
            throw new EntityNotFoundException("Le site existe déjà !");
        }
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

    public Long getCount(){
        return  siteTouristiqueRepository.count();
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