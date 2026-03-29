package egate.digital.fasotour.services;

import egate.digital.fasotour.security.refreshtoken.RefreshToken;
import egate.digital.fasotour.security.refreshtoken.RefreshTokenService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import egate.digital.fasotour.dto.auth.AuthResponseDTO;
import egate.digital.fasotour.dto.auth.LoginRequestDTO;
import egate.digital.fasotour.dto.user.*;
import egate.digital.fasotour.mappers.AgenceMapper;
import egate.digital.fasotour.mappers.GuideMapper;
import egate.digital.fasotour.mappers.TouristeMapper;
import egate.digital.fasotour.model.*;
import egate.digital.fasotour.model.valide.Validate;
import egate.digital.fasotour.repository.*;
import egate.digital.fasotour.security.jwt.JwtService;
import egate.digital.fasotour.services.valide.ValidateService;

import java.time.Instant;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AuthService {

    private final UtilisateurRepository utilisateurRepository;
    private final TouristeRepository touristeRepository;
    private final GuideRepository guideRepository;
    private final AgenceRepository agenceRepository;
    private final RoleRepository roleRepository;
    private final LangueRepository langueRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final ValidateService validateService;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final @Lazy RefreshTokenService refreshTokenService;


    //Connexion



    // Connexion
    @Transactional
    public AuthResponseDTO connecter(LoginRequestDTO dto, String ipAddress, String userAgent) {
        Authentication auth = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(dto.mail(), dto.motDePasse())
        );

        Utilisateur utilisateur = (Utilisateur) auth.getPrincipal();

        if (!utilisateur.getActif())
            throw new EntityNotFoundException(
                    "Compte non activé. Vérifiez votre mail : " + utilisateur.getMail());

        String accessToken = jwtService.generateToken(utilisateur);
        RefreshToken refreshToken = refreshTokenService.createTokenForNewSession(utilisateur, ipAddress, userAgent);

        List<String> roles = utilisateur.getRoles()
                .stream()
                .map(Role::getRoles)
                .collect(Collectors.toList());

        return new AuthResponseDTO(accessToken, refreshToken.getToken(),utilisateur.getId(),
                utilisateur.getMail(), utilisateur.getNomComplet(), roles);
    }    // Inscription
    @Transactional
    public void inscrireTouriste(TouristeRequestDTO dto) {
        verifierMailDisponible(dto.mail());

        Touriste touriste = new Touriste();
        touriste.setNomComplet(dto.nomComplet());
        touriste.setAdresse(dto.adresse());
        touriste.setMail(dto.mail());
        touriste.setTelephone(dto.telephone());
        touriste.setMotDePasse(passwordEncoder.encode(dto.motDePasse()));
        touriste.setPays(dto.pays());
        touriste.setBio(dto.bio());
        touriste.setActif(true);
        touriste.setPreferenceTouristique(dto.preferenceTouristique());
        touriste.setLangues(resolverLangues(dto.langueIds()));
        touriste.setRoles(dto.roleIds() != null && !dto.roleIds().isEmpty()
                ? resolverRoles(dto.roleIds())
                : Set.of(getRoleByNom("TOURISTE")));

        Touriste saved = touristeRepository.save(touriste);
        validateService.enregistrer(saved);
    }

    @Transactional
    public void inscrireGuide(GuideRequestDTO dto) {
        verifierMailDisponible(dto.mail());

        Guide guide = GuideMapper.toEntity(dto,
                resolverLangues(dto.langueIds()),
                dto.roleIds() != null && !dto.roleIds().isEmpty()
                        ? resolverRoles(dto.roleIds())
                        : Set.of(getRoleByNom("GUIDE")),
                resolverAgences(dto.agenceIds()));

        guide.setMotDePasse(passwordEncoder.encode(dto.motDePasse()));
        guide.setActif(true);

        Guide saved = guideRepository.save(guide);
        validateService.enregistrer(saved);
    }

    @Transactional
    public void inscrireAgence(AgenceRequestDTO dto) {
        verifierMailDisponible(dto.mail());

        Agence agence = AgenceMapper.toEntity(dto,
                resolverLangues(dto.langueIds()),
                dto.roleIds() != null && !dto.roleIds().isEmpty()
                        ? resolverRoles(dto.roleIds())
                        : Set.of(getRoleByNom("AGENCE")),
                resolverGuides(dto.guideIds()));

        agence.setMotDePasse(passwordEncoder.encode(dto.motDePasse()));
        agence.setActif(true);

        Agence saved = agenceRepository.save(agence);
        validateService.enregistrer(saved);
    }

    //Activation
    @Transactional
    public void activer(String code) {
        Validate validate = validateService.redCode(code);

        if (Instant.now().isAfter(validate.getExpired()))
            throw new RuntimeException("Code expiré. Demandez un nouveau code.");

        if (validate.getActivated() != null)
            throw new RuntimeException("Ce code a déjà été utilisé.");

        Utilisateur utilisateur = utilisateurRepository
                .findById(validate.getUtilisateur().getId())
                .orElseThrow(() -> new EntityNotFoundException("Utilisateur introuvable"));

        utilisateur.setActif(true);
        validate.setActivated(Instant.now());
        utilisateurRepository.save(utilisateur);
    }

    @Transactional
    public void renvoyerCode(String mail) {
        Utilisateur utilisateur = utilisateurRepository.findByMail(mail)
                .orElseThrow(() -> new EntityNotFoundException("Mail introuvable"));

        if (utilisateur.getActif())
            throw new RuntimeException("Ce compte est déjà activé.");

        validateService.enregistrer(utilisateur);
    }

    //CRUD TOuriste
    public List<TouristeResponseDTO> getAllTouristes() {
        return touristeRepository.findAll()
                .stream()
                .map(TouristeMapper::toDTO)
                .collect(Collectors.toList());
    }

    // Récupérer tous les touristes par ordre alphabétique
    @Transactional
    public List<TouristeResponseDTO> getAllTouristesAlphabetically() {
        return touristeRepository.findAllOrderedAlphabetically()
                .stream()
                .map(TouristeMapper::toDTO)
                .collect(Collectors.toList());
    }

    // Récupérer un touriste par mail
    public Optional<TouristeResponseDTO> getTouristeByMail(String mail) {
        return touristeRepository.findByMail(mail)
                .map(TouristeMapper::toDTO);
    }

    public TouristeResponseDTO getTouristeById(Long id) {
        return touristeRepository.findById(id)
                .map(TouristeMapper::toDTO)
                .orElseThrow(() -> new EntityNotFoundException("Touriste introuvable : " + id));
    }

    @Transactional
    public TouristeResponseDTO updateTouriste(Long id, TouristeRequestDTO dto) {
        Touriste touriste = touristeRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Touriste introuvable : " + id));

        touriste.setNomComplet(dto.nomComplet());
        touriste.setAdresse(dto.adresse());
        touriste.setTelephone(dto.telephone());
        touriste.setPays(dto.pays());
        touriste.setBio(dto.bio());
        touriste.setPreferenceTouristique(dto.preferenceTouristique());
        touriste.setLangues(resolverLangues(dto.langueIds()));

        if (dto.motDePasse() != null && !dto.motDePasse().isBlank())
            touriste.setMotDePasse(passwordEncoder.encode(dto.motDePasse()));

        return TouristeMapper.toDTO(touristeRepository.save(touriste));
    }

    @Transactional
    public void deleteTouriste(Long id) {
        if (!touristeRepository.existsById(id))
            throw new EntityNotFoundException("Touriste introuvable : " + id);

        touristeRepository.deleteReservationsByTouristeId(id);
        touristeRepository.deleteById(id);
    }

    @Transactional
    public void toggleTouriste(Long id) {
        Touriste touriste = touristeRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Touriste introuvable : " + id));
        touriste.setActif(!touriste.getActif());
        touristeRepository.save(touriste);
    }

    //CRUD Guide

    public List<GuideResponseDTO> getAllGuides() {
        return guideRepository.findAll()
                .stream()
                .map(GuideMapper::toDTO)
                .collect(Collectors.toList());
    }

    public GuideResponseDTO getGuideById(Long id) {
        return guideRepository.findById(id)
                .map(GuideMapper::toDTO)
                .orElseThrow(() -> new EntityNotFoundException("Guide introuvable : " + id));
    }

    public List<GuideResponseDTO> getAllGuidesorderAlp() {
        return guideRepository.findAllOrderByNomCompletAsc()
                .stream()
                .map(GuideMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Transactional
    public GuideResponseDTO updateGuide(Long id, GuideRequestDTO dto) {
        Guide guide = guideRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Guide introuvable : " + id));

        guide.setNomComplet(dto.nomComplet());
        guide.setAdresse(dto.adresse());
        guide.setTelephone(dto.telephone());
        guide.setPays(dto.pays());
        guide.setBio(dto.bio());
        guide.setExperience(dto.experience());
        guide.setPreferenceTouristique(dto.preferenceTouristique());
        guide.setLangues(resolverLangues(dto.langueIds()));
        guide.setAgences(resolverAgences(dto.agenceIds()));

        if (dto.motDePasse() != null && !dto.motDePasse().isBlank())
            guide.setMotDePasse(passwordEncoder.encode(dto.motDePasse()));

        return GuideMapper.toDTO(guideRepository.save(guide));
    }

    @Transactional
    public void deleteGuide(Long id) {
        Guide guide = guideRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Guide introuvable : " + id));

        guideRepository.deleteCircuitGuideByGuideId(id);

        // 2. Nettoyer agence_guide directement en base
        guideRepository.deleteAgenceGuideByGuideId(id);

        // 3. Supprimer proprement
        guideRepository.delete(guide);
    }

    @Transactional
    public void toggleGuide(Long id) {
        Guide guide = guideRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Guide introuvable : " + id));
        guide.setActif(!guide.getActif());
        guideRepository.save(guide);
    }

    // CRUD Agence
    public List<AgenceResponseDTO> getAllAgences() {
        return agenceRepository.findAll()
                .stream()
                .map(AgenceMapper::toDTO)
                .collect(Collectors.toList());
    }

    // GET all (ordre alphabétique)
    @Transactional
    public List<AgenceResponseDTO> getAllAgencesorder() {
        return agenceRepository.findAllOrderedAlphabetically()
                .stream()
                .map(AgenceMapper::toDTO)
                .collect(Collectors.toList());
    }

    public AgenceResponseDTO getAgenceById(Long id) {
        return agenceRepository.findById(id)
                .map(AgenceMapper::toDTO)
                .orElseThrow(() -> new EntityNotFoundException("Agence introuvable : " + id));
    }

    @Transactional
    public AgenceResponseDTO updateAgence(Long id, AgenceRequestDTO dto) {
        Agence agence = agenceRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Agence introuvable : " + id));

        agence.setNomComplet(dto.nomComplet());
        agence.setAdresse(dto.adresse());
        agence.setTelephone(dto.telephone());
        agence.setPays(dto.pays());
        agence.setBio(dto.bio());
        agence.setSitWeb(dto.sitWeb());
        agence.setPageFacebook(dto.pageFacebook());
        agence.setNumeroAgrement(dto.numeroAgrement());
        agence.setDateCreation(dto.dateCreation());
        agence.setLangues(resolverLangues(dto.langueIds()));
        agence.setGuides(resolverGuides(dto.guideIds()));

        if (dto.motDePasse() != null && !dto.motDePasse().isBlank())
            agence.setMotDePasse(passwordEncoder.encode(dto.motDePasse()));

        return AgenceMapper.toDTO(agenceRepository.save(agence));
    }

    @Transactional
    public void deleteAgence(Long id) {
        if (!agenceRepository.existsById(id))
            throw new EntityNotFoundException("Agence introuvable : " + id);

        agenceRepository.deleteAgenceGuideByAgenceId(id);

        agenceRepository.deleteCircuitGuideByAgenceId(id);

        agenceRepository.deleteById(id);
    }

    @Transactional
    public void toggleAgence(Long id) {
        Agence agence = agenceRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Agence introuvable : " + id));
        agence.setActif(!agence.getActif());
        agenceRepository.save(agence);
    }

    //Helps
    private void verifierMailDisponible(String mail) {
        if (utilisateurRepository.findByMail(mail).isPresent())
            throw new RuntimeException("Cet email est déjà utilisé.");
    }

    private Set<Langue> resolverLangues(Set<Long> ids) {
        if (ids == null || ids.isEmpty()) return new HashSet<>();
        return ids.stream()
                .map(id -> langueRepository.findById(id)
                        .orElseThrow(() -> new EntityNotFoundException("Langue introuvable : " + id)))
                .collect(Collectors.toSet());
    }

    private Set<Role> resolverRoles(Set<Long> ids) {
        if (ids == null || ids.isEmpty()) return new HashSet<>();
        return ids.stream()
                .map(id -> roleRepository.findById(id)
                        .orElseThrow(() -> new EntityNotFoundException("Rôle introuvable : " + id)))
                .collect(Collectors.toSet());
    }

    private Set<Agence> resolverAgences(Set<Long> ids) {
        if (ids == null || ids.isEmpty()) return new HashSet<>();
        return ids.stream()
                .map(id -> agenceRepository.findById(id)
                        .orElseThrow(() -> new EntityNotFoundException("Agence introuvable : " + id)))
                .collect(Collectors.toSet());
    }

    private Set<Guide> resolverGuides(Set<Long> ids) {
        if (ids == null || ids.isEmpty()) return new HashSet<>();
        return ids.stream()
                .map(id -> guideRepository.findById(id)
                        .orElseThrow(() -> new EntityNotFoundException("Guide introuvable : " + id)))
                .collect(Collectors.toSet());
    }

    private Role getRoleByNom(String nom) {
        return roleRepository.findByRoles(nom)
                .orElseThrow(() -> new EntityNotFoundException("Rôle introuvable : " + nom));
    }

}