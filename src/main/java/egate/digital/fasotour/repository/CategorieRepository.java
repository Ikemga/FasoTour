package egate.digital.fasotour.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import egate.digital.fasotour.model.Categorie;


public interface CategorieRepository extends  JpaRepository<Categorie, Long> {

    Optional<Categorie> findByCategorie(String categorie);
    boolean existsByCategorie(String categorie);

}
