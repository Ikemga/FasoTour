package egate.digital.fasotour.repository;


import org.springframework.data.jpa.repository.JpaRepository;

import egate.digital.fasotour.model.Agence;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface AgenceRepository extends JpaRepository<Agence, Long> {

    @Query("SELECT a FROM Agence a ORDER BY a.nomComplet ASC")
    List<Agence> findAllOrderedAlphabetically();
}
