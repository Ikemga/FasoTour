package egate.digital.fasotour.services;

import egate.digital.fasotour.dto.UtilisateurDTO;
import egate.digital.fasotour.mappers.UtilisateurMapper;
import egate.digital.fasotour.repository.UtilisateurRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UtilisateurService {

    private final UtilisateurRepository utilisateurRepository;
    private final UtilisateurMapper utilisateurMapper; // ← injecté

    public UtilisateurService(UtilisateurRepository utilisateurRepository,
                              UtilisateurMapper utilisateurMapper) {
        this.utilisateurRepository = utilisateurRepository;
        this.utilisateurMapper = utilisateurMapper;
    }

    // Tous les utilisateurs
    public List<UtilisateurDTO> getAllUsers() {
        return utilisateurRepository.findAll()
                .stream()
                .map(utilisateurMapper::toDTO)
                .collect(Collectors.toList());
    }

    // Plus récent
    public List<UtilisateurDTO> getUsersByRecent() {
        return utilisateurRepository.findUsersByRecent()
                .stream()
                .map(utilisateurMapper::toDTO)
                .collect(Collectors.toList());
    }

    // A → Z
    public List<UtilisateurDTO> getUsersByNameAsc() {
        return utilisateurRepository.findUsersByNameAsc()
                .stream()
                .map(utilisateurMapper::toDTO)
                .collect(Collectors.toList());
    }

    // Z → A
    public List<UtilisateurDTO> getUsersByNameDesc() {
        return utilisateurRepository.findUsersByNameDesc()
                .stream()
                .map(utilisateurMapper::toDTO)
                .collect(Collectors.toList());
    }

    // Compter par rôle
    public long countUsersByRole(String role) {
        return utilisateurRepository.countByRole(role);
    }

    // Nombre total d'utilisateurs
    public Long getCountAllUser() {
        return utilisateurRepository.count();
    }

    public List<UtilisateurDTO> searchUsers(String search, Boolean actif) {
        return utilisateurRepository.searchUsers(search, actif)
                .stream()
                .map(utilisateurMapper::toDTO)
                .collect(Collectors.toList());
    }


}