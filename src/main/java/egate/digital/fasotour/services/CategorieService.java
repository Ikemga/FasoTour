package egate.digital.fasotour.services;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import egate.digital.fasotour.dto.site.CategorieRequestDTO;
import egate.digital.fasotour.dto.site.CategorieResponseDTO;
import egate.digital.fasotour.mappers.CategorieMapper;
import egate.digital.fasotour.model.Categorie;
import egate.digital.fasotour.repository.CategorieRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CategorieService {

    private final CategorieRepository categorieRepository;

    @Transactional
    public CategorieResponseDTO saveCategorie(CategorieRequestDTO dto) {
        if (categorieRepository.existsByCategorie(dto.categorie()))
            throw new EntityNotFoundException("Cette catégorie existe déjà !");

        Categorie categorie = CategorieMapper.toEntity(dto);
        return CategorieMapper.toResponseDTO(categorieRepository.save(categorie));
    }

    public List<CategorieResponseDTO> getAllCategorie() {
        return categorieRepository.findAll()
                .stream()
                .map(CategorieMapper::toResponseDTO)
                .collect(Collectors.toList());
    }

    public CategorieResponseDTO getCategorieById(Long id) {
        return categorieRepository.findById(id)
                .map(CategorieMapper::toResponseDTO)
                .orElseThrow(() -> new EntityNotFoundException("Catégorie introuvable : " + id));
    }

    public CategorieResponseDTO getByNom(String categorie) {
        return categorieRepository.findByCategorie(categorie)
                .map(CategorieMapper::toResponseDTO)
                .orElseThrow(() -> new EntityNotFoundException("Catégorie introuvable : " + categorie));
    }

    @Transactional
    public CategorieResponseDTO updateCategorie(Long id, CategorieRequestDTO dto) {
        Categorie categorie = categorieRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Catégorie introuvable : " + id));

        categorie.setCategorie(dto.categorie());
        categorie.setDescription(dto.description());
        categorie.setStatut(dto.statut());

        return CategorieMapper.toResponseDTO(categorieRepository.save(categorie));
    }

    @Transactional
    public void deleteCategorie(Long id) {
        if (!categorieRepository.existsById(id))
            throw new EntityNotFoundException("Catégorie introuvable : " + id);
        categorieRepository.deleteById(id);
    }
}