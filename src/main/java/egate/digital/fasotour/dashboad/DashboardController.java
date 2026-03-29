package egate.digital.fasotour.dashboad;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/dashboard")
@RequiredArgsConstructor
public class DashboardController {

    private final DashboardService dashboardService;
    private final UserDashboardService userDashboardService;

    @GetMapping("/all")
    public DashboardDTO getDashboard() {
        return dashboardService.getDashboard();
    }

    @GetMapping("/utilisateur")
    public UserDashboardStatDTO getStats() {
        return userDashboardService.getStats();
    }
}
