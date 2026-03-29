package egate.digital.fasotour.dashboad;

public record UserDashboardStatDTO(
        long totalUtilisateurs,
        long utilisateursActifs,
        long totalGuides,
        long totalTouristes,
        long totalAgences) {
}
