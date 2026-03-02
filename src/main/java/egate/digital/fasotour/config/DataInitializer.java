
/*package egate.digital.fasotour.config;

import egate.digital.fasotour.model.*;
import egate.digital.fasotour.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
@RequiredArgsConstructor
@Slf4j
public class DataInitializer implements CommandLineRunner {

    private final UtilisateurRepository utilisateurRepository;
    private final RoleRepository roleRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) {

        Role roleAdmin = roleRepository.findByRoles("ADMIN")
                .orElseGet(() -> {
                    Role r = new Role();
                    r.setRoles("ADMIN");
                    r.setDescription("Administrateur principal");
                    r.setStatut("ACTIVE");
                    log.info("Rôle ADMIN créé.");
                    return roleRepository.save(r);
                });

        if (utilisateurRepository.findByMail("admin@fasotour.bf").isEmpty()) {

            Touriste admin = new Touriste(); // ou Utilisateur selon ta hiérarchie
            admin.setNomComplet("Administrateur FasoTour");
            admin.setMail("admin@fasotour.bf");
            admin.setMotDePasse(passwordEncoder.encode("Admin@2024!"));
            admin.setTelephone("+226 67 54 32 91");
            admin.setPays("Burkina Faso");
            admin.setActif(true);
            admin.setRoles(Set.of(roleAdmin));

            utilisateurRepository.save(admin);
            log.info("Admin créé → mail: admin@fasotour.bf | mdp: Admin@2024!");
        } else {
            log.info("Admin déjà existant.");
        }
    }
}
*/