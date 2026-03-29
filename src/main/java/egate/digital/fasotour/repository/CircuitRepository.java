package egate.digital.fasotour.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import egate.digital.fasotour.model.Circuit;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;
import java.util.Optional;

public interface CircuitRepository extends JpaRepository<Circuit, Long>{

    Boolean existsByCircuitName(String circuitName);
    Optional<Circuit> findByCircuitName(String circuitName);

    Optional<Circuit> findByAgenceId(Long agenceId);

    Optional<Circuit> findByStatut(String statut);
    Long countByStatut(String statut);

    @Query("SELECT c FROM Circuit c ORDER BY c.createdAt DESC")
    List<Circuit> findAllOrderByCreatedAtDesc();

    @Query("SELECT c FROM Circuit c ORDER BY c.createdAt ASC")
    List<Circuit> findAllOrderByCreatedAtAsc();

    @Query("SELECT c FROM Circuit c ORDER BY c.circuitName ASC")
    List<Circuit> findAllOrderByNameAsc();

    @Query("SELECT c FROM Circuit c ORDER BY c.circuitName DESC")
    List<Circuit> findAllOrderByNameDesc();

    @Query("SELECT c FROM Circuit c WHERE LOWER(c.circuitName) LIKE LOWER(CONCAT('%', :circuitName, '%'))")
    List<Circuit> searchByName(@Param("circuitName") String circuitName);

}
