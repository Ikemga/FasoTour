package egate.digital.fasotour.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import egate.digital.fasotour.model.Touriste;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface TouristeRepository extends   JpaRepository<Touriste, Long> {

    Optional<Touriste> findByMail(String Mail);

    @Query("SELECT t FROM Touriste t ORDER BY t.nomComplet ASC")
    List<Touriste> findAllOrderedAlphabetically();

}
