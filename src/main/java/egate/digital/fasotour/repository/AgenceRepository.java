package egate.digital.fasotour.repository;


import org.springframework.data.jpa.repository.JpaRepository;

import egate.digital.fasotour.model.Agence;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface AgenceRepository extends JpaRepository<Agence, Long> {

    @Query("SELECT a FROM Agence a ORDER BY a.nomComplet ASC")
    List<Agence> findAllOrderedAlphabetically();

    // Nettoyer agence_guide
    @Modifying
    @Query(value = "DELETE FROM agence_guide WHERE agence_id = :agenceId", nativeQuery = true)
    void deleteAgenceGuideByAgenceId(@Param("agenceId") Long agenceId);

    // Nettoyer circuit_guide pour tous les circuits de l'agence
    @Modifying
    @Query(value = "DELETE FROM circuit_guide WHERE circuit_id IN " +
            "(SELECT id FROM circuit WHERE agence_id = :agenceId)", nativeQuery = true)
    void deleteCircuitGuideByAgenceId(@Param("agenceId") Long agenceId);

}
