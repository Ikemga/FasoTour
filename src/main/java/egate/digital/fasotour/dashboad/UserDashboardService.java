package egate.digital.fasotour.dashboad;

import egate.digital.fasotour.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserDashboardService {

    private final UtilisateurRepository utilisateurRepository;
    private final GuideRepository guideRepository;
    private final TouristeRepository touristeRepository;
    private final AgenceRepository agenceRepository;

    public UserDashboardStatDTO getStats() {
        long totalUtilisateurs = utilisateurRepository.count();
        long utilisateursActifs = utilisateurRepository.countByActifTrue();
        long totalGuides = guideRepository.count();
        long totalTouristes = touristeRepository.count();
        long totalAgences = agenceRepository.count();

        return new UserDashboardStatDTO(
                totalUtilisateurs,
                utilisateursActifs,
                totalGuides,
                totalTouristes,
                totalAgences
        );
    }
}