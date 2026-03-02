package egate.digital.fasotour.services;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import egate.digital.fasotour.model.Langue;
import egate.digital.fasotour.repository.LangueRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class LanguesService {

    private final LangueRepository langueRepository;

    @Transactional
    public Langue saveLangue(Langue langue) {
        if (langueRepository.findByLangues(langue.getLangues()).isPresent())
            throw new RuntimeException("La langue existe déjà !");
        return langueRepository.save(langue);
    }

    public List<Langue> getAllLangues() {
        return langueRepository.findAll();
    }

    public Langue getLangueById(Long id) {
        return langueRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Langue introuvable : " + id));
    }

    public Langue getByLangues(String langues) {
        return langueRepository.findByLangues(langues)
                .orElseThrow(() -> new EntityNotFoundException("Langue introuvable : " + langues));
    }

    public Langue getByCode(String code) {
        return langueRepository.findByCode(code)
                .orElseThrow(() -> new EntityNotFoundException("Langue introuvable : " + code));
    }

    @Transactional
    public Langue updateLangue(Long id, Langue langue) {
        Langue existing = langueRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Langue introuvable : " + id));
        existing.setLangues(langue.getLangues());
        existing.setCode(langue.getCode());
        return langueRepository.save(existing);
    }

    @Transactional
    public void deleteLangue(Long id) {
        if (!langueRepository.existsById(id))
            throw new EntityNotFoundException("Langue introuvable : " + id);
        langueRepository.deleteById(id);
    }
}