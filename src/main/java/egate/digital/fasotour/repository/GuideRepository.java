package egate.digital.fasotour.repository;

import egate.digital.fasotour.model.Agence;
import org.springframework.data.jpa.repository.JpaRepository;

import egate.digital.fasotour.model.Avis;
import egate.digital.fasotour.model.Guide;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface GuideRepository extends JpaRepository<Guide, Long> {

    @Query("""
        SELECT g FROM Guide g
        JOIN FETCH g.langues
        ORDER BY LOWER(g.nomComplet) ASC
    """)
    List<Guide> findAllOrderByNomCompletAsc();

    @Query("SELECT g FROM Guide g ORDER BY g.nomComplet ASC")
    List<Guide> findAllOrderedAlphabetically();

    @Query("SELECT g FROM Guide g LEFT JOIN FETCH g.agences WHERE g.id = :id")
    Optional<Guide> findByIdWithRelations(@Param("id") Long id);

    @Modifying
    @Query(value = "DELETE FROM circuit_guide WHERE guide_id = :guideId", nativeQuery = true)
    void deleteCircuitGuideByGuideId(@Param("guideId") Long guideId);

    @Modifying
    @Query(value = "DELETE FROM agence_guide WHERE guide_id = :guideId", nativeQuery = true)
    void deleteAgenceGuideByGuideId(@Param("guideId") Long guideId);
}
