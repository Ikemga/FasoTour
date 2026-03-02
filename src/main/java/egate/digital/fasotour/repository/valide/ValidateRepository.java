package egate.digital.fasotour.repository.valide;

import org.springframework.data.repository.CrudRepository;

import egate.digital.fasotour.model.valide.Validate;

import java.util.Optional;

public interface ValidateRepository extends CrudRepository<Validate, Long> {
    Optional<Validate> findByCode(String code);

}
