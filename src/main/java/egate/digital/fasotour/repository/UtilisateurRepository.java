package egate.digital.fasotour.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import egate.digital.fasotour.model.Utilisateur;

import java.util.Optional;

@Repository
public interface UtilisateurRepository extends JpaRepository<Utilisateur, Long> {
    Optional<Utilisateur> findByMail(String mail);


    @Query("""
    SELECT COUNT(u)
    FROM Utilisateur u
    JOIN u.roles r
    WHERE r.roles = :role
""")
    long countByRole(@Param("role") String role);
}