package egate.digital.fasotour.dashboad;

import egate.digital.fasotour.repository.CircuitRepository;
import egate.digital.fasotour.repository.ReservationRepository;
import egate.digital.fasotour.repository.SiteTouristiqueRepository;
import egate.digital.fasotour.repository.UtilisateurRepository;
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


    public Long getCount(){
        return  siteTouristiqueRepository.count();
    }

    Long getCountReservationByMonth(){
        LocalDate now = LocalDate.now();
        return reservationRepository.countByMoisEtAnnee(
                now.getMonthValue(),
                now.getYear()
        );
    }

    Long getCountByStatus(){
        return circuitRepository.countByStatut("ACTIF");
    }

    public long getUserCountByRole(String role) {
        return utilisateurRepository.countByRole(role);
    }
}
