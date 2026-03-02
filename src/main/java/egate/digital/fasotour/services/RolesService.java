package egate.digital.fasotour.services;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import egate.digital.fasotour.dto.user.RoleRequestDTO;
import egate.digital.fasotour.dto.user.RoleResponseDTO;
import egate.digital.fasotour.mappers.RoleMapper;
import egate.digital.fasotour.model.Role;
import egate.digital.fasotour.repository.RoleRepository;

import java.util.List;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class RolesService {

    private final RoleRepository roleRepository;
    //Save
    @Transactional
    public RoleResponseDTO saveRole(RoleRequestDTO dto){
        if(roleRepository.existsByRoles(dto.role())){
            throw new EntityNotFoundException("Ce rôle existe déjà :" + dto.role());
        }
        Role role = RoleMapper.toEntity(dto);
        Role saved = roleRepository.save(role);

        return RoleMapper.toDTO(saved);
    }

    //get
    public List<RoleResponseDTO> getAll(){
        try{
            List<RoleResponseDTO> roles =  roleRepository.findAll()
                    .stream()
                    .map(RoleMapper::toDTO)
                    .toList();
            if (roles.isEmpty()){
                throw new RuntimeException("Aucun rôle disponible");
            }
            return  roles;
        }catch ( RuntimeException e){
            throw e;
        }catch (Exception e){
            throw new RuntimeException(("Erreur lors de la récupération des rôles : " + e.getMessage()));
        }

    }

    // get by id
    public RoleResponseDTO findById(Long id) {
        return roleRepository.findById(id)
                .map(RoleMapper::toDTO)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Aucun rôle introuvé avec l'id : " + id));
    }
    //get by role
    public RoleResponseDTO findByRole(String roles){
        Role role = roleRepository.findByRoles(roles)
                .orElseThrow(() -> new EntityNotFoundException("Aucun rôle introuvé avec : " + roles));

        return RoleMapper.toDTO(role);
    }
    //Up
    @Transactional
    public RoleResponseDTO updateRole(Long id, RoleRequestDTO dto) {
        Role role = roleRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Aucun rôle introuvé avec l'id : " + id));

        role.setRoles(dto.role());
        role.setDescription(dto.description());
        role.setStatut(dto.statut());

        Role rolesave =roleRepository.save(role);

        return RoleMapper.toDTO(rolesave);
    }
    //delete
    @Transactional
    public void deleteRole(Long id){

        if(!roleRepository.existsById(id)){
            throw new EntityNotFoundException("Aucun rôle introuvé avec l'id : " + id);
        }
        roleRepository.deleteById(id);
    }
    // Activer - Désactiver
    @Transactional
    public RoleResponseDTO Statut(Long id) {
        Role role = roleRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Rôle introuvable avec l'id : " + id));

        role.setStatut(role.getStatut().equals("ACTIF") ? "INACTIF" : "ACTIF");
        return RoleMapper.toDTO(roleRepository.save(role));
    }
}