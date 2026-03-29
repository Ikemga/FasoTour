package egate.digital.fasotour.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import egate.digital.fasotour.model.Reservation;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface ReservationRepository extends JpaRepository<Reservation, Long>{
    List<Reservation> findByStatut(String statut);

    List<Reservation> findByDateResevation(LocalDate dateResevation);

    // Count by moth

    @Query("SELECT COUNT(r) FROM Reservation r " +
            "WHERE MONTH(r.dateResevation) = :mois " +
            "AND YEAR(r.dateResevation) = :annee")
    long countByMoisEtAnnee(@Param("mois") int mois,
                            @Param("annee") int annee);

}
