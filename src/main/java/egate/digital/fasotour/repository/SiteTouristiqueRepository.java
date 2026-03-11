package egate.digital.fasotour.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import egate.digital.fasotour.model.SiteTouristique;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;


public interface SiteTouristiqueRepository extends JpaRepository<SiteTouristique, Long> {

    List<SiteTouristique> findByRegionIgnoreCase(String region);
    boolean existsByNom(String nom);
    List<SiteTouristique> findAllByOrderByCreatedAtDesc();

    @Query("""
    SELECT DISTINCT s FROM SiteTouristique s
    LEFT JOIN s.categories c
    WHERE (:q IS NULL OR LOWER(s.nom)     LIKE LOWER(CONCAT('%', :q, '%')))
       OR (:q IS NULL OR LOWER(s.region)  LIKE LOWER(CONCAT('%', :q, '%')))
       OR (:q IS NULL OR LOWER(c.categorie) LIKE LOWER(CONCAT('%', :q, '%')))
""")
    List<SiteTouristique> search(@Param("q") String q);
}

