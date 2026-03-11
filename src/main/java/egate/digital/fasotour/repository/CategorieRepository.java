package egate.digital.fasotour.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import egate.digital.fasotour.model.Categorie;
import org.springframework.data.jpa.repository.Query;


public interface CategorieRepository extends  JpaRepository<Categorie, Long> {

    Optional<Categorie> findByCategorie(String categorie);
    boolean existsByCategorie(String categorie);

    @Query(
            "SELECT c FROM Categorie c ORDER BY c.createdAt DESC"
        )
    List<Categorie> findAllNewestFirst();

    @Query(
            "SELECT c FROM Categorie c ORDER BY c.categorie ASC"
        )
    List<Categorie> findAllAlphabetical();
}
