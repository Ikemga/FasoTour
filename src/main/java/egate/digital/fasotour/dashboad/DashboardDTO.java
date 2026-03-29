package egate.digital.fasotour.dashboad;

public record DashboardDTO(
        long circuitsActifs,
        long totalSites,
        long totalGuides,
        long totalAgences,
        long totalTouristes,
        long reservationsMois,
        double noteMoyenne
) {}
