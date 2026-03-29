package egate.digital.fasotour.dashboad;

import egate.digital.fasotour.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class DashboardService {

    private final SiteTouristiqueRepository siteTouristiqueRepository;
    private final CircuitRepository circuitRepository;
    private final ReservationRepository reservationRepository;
    private final UtilisateurRepository utilisateurRepository;
    private  final  AvisRepository avisRepository;

    public DashboardDTO getDashboard() {

        LocalDate now = LocalDate.now();

        long totalSites = siteTouristiqueRepository.count();

        long circuitsActifs = circuitRepository.countByStatut("ACTIF");

        long totalGuides = utilisateurRepository.countByRole("GUIDE");
        long totalAgences = utilisateurRepository.countByRole("AGENCE");
        long totalTouristes = utilisateurRepository.countByRole("TOURISTE");

        long reservationsMois = reservationRepository.countByMoisEtAnnee(
                now.getMonthValue(),
                now.getYear()
        );

        Double noteMoyenne = avisRepository.moyenneNotes();

        return new DashboardDTO(
                circuitsActifs,
                totalSites,
                totalGuides,
                totalAgences,
                totalTouristes,
                reservationsMois,
                noteMoyenne != null ? noteMoyenne : 0.0
        );
    }
}