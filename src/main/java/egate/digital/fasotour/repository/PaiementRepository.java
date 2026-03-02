package egate.digital.fasotour.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import egate.digital.fasotour.model.Paiement;

import java.util.List;

public interface PaiementRepository extends JpaRepository<Paiement, Long>{
    List<Paiement> findByStatut(String statut);

}
