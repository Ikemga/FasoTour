package egate.digital.fasotour.dashboad;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@AllArgsConstructor
@RestController
@RequestMapping("v1/dashboard")
public class DashboardController {

    private final DashboardService dashboardService;

    @GetMapping("sites/count")
    public ResponseEntity<Long> countSites() {
        return ResponseEntity.ok(dashboardService.getCount());
    }

    @GetMapping("circuits/actifs")
    public ResponseEntity<Long> getCircuitsActifs() {
        return ResponseEntity.ok(dashboardService.getCountByStatus());
    }

    @GetMapping("reservations/mois")
    public ResponseEntity<Long> getReservationsMoisCourant() {
        return ResponseEntity.ok(dashboardService.getCountReservationByMonth());
    }

    @GetMapping("utilisateurs/countby/role")
    public ResponseEntity<Long> getUsersCountByRole(@RequestParam String role) {
        return ResponseEntity.ok(dashboardService.getUserCountByRole(role));
    }
}