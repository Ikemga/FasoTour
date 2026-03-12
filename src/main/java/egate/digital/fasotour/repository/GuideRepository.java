package egate.digital.fasotour.repository;

import egate.digital.fasotour.model.Agence;
import org.springframework.data.jpa.repository.JpaRepository;

import egate.digital.fasotour.model.Avis;
import egate.digital.fasotour.model.Guide;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface GuideRepository extends JpaRepository<Guide, Long> {

    @Query("""
        SELECT g FROM Guide g
        JOIN FETCH g.langues
        ORDER BY LOWER(g.nomComplet) ASC
    """)
    List<Guide> findAllOrderByNomCompletAsc();

    @Query("SELECT g FROM Guide g ORDER BY g.nomComplet ASC")
    List<Guide> findAllOrderedAlphabetically();

}
