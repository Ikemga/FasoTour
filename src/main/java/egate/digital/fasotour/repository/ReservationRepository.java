package egate.digital.fasotour.repository;

import egate.digital.fasotour.dto.StatiqueDTO;
import org.springframework.data.jpa.repository.JpaRepository;

import egate.digital.fasotour.model.Reservation;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.List;

public interface ReservationRepository extends JpaRepository<Reservation, Long>{
    List<Reservation> findByStatut(String statut);

    List<Reservation> findByDateResevation(LocalDate dateResevation);
/*
    @Query("""
        SELECT new egate.digital.fasotour.dto.site.ReservationStatsDTO(
            r.circuit.id,
            r.circuit.circuitName,
            COUNT(r)
        )
        FROM Reservation r
        GROUP BY r.circuit.id, r.circuit.circuitName
    """)
    List<StatiqueDTO> countReservationsByCircuit();
    */
}
