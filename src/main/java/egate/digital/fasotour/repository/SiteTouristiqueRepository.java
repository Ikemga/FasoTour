package egate.digital.fasotour.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import egate.digital.fasotour.model.SiteTouristique;

import java.util.List;


public interface SiteTouristiqueRepository extends JpaRepository<SiteTouristique, Long> {

    List<SiteTouristique> findByRegionIgnoreCase(String region);
    boolean existsByNom(String nom);

}
