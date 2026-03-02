package egate.digital.fasotour.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import egate.digital.fasotour.model.Circuit;

import java.util.Collection;
import java.util.Optional;

public interface CircuitRepository extends JpaRepository<Circuit, Long>{
    Boolean existsByCircuitName(String circuitName);
    Optional<Circuit> findByCircuitName(String circuitName);

    Optional<Circuit> findByAgenceId(Long agenceId);

    Optional<Circuit> findByStatut(String statut);
}
