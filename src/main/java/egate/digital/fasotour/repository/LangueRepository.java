package egate.digital.fasotour.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import egate.digital.fasotour.model.Langue;

import java.util.Optional;

public interface LangueRepository extends JpaRepository<Langue, Long>{


    Optional<Langue> findByLangues(String langues);
    Optional<Langue> findByCode(String code);

}
