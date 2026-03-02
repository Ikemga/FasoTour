package egate.digital.fasotour.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import egate.digital.fasotour.model.Touriste;

import java.util.Optional;

public interface TouristeRepository extends   JpaRepository<Touriste, Long> {

    Optional<Touriste> findByMail(String Mail);

}
