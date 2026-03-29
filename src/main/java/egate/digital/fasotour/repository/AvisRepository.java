package egate.digital.fasotour.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import egate.digital.fasotour.model.Avis;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface AvisRepository extends JpaRepository<Avis, Long> {
    List<Avis> findByCircuitId(Long circuitId);
    List<Avis> findByGuideId(Long guideId);
    @Query("SELECT AVG(a.note) FROM Avis a")
    Double moyenneNotes();


}
