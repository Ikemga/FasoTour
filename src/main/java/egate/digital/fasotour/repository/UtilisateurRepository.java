package egate.digital.fasotour.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import egate.digital.fasotour.model.Utilisateur;

import java.util.List;
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

    @Query("""
    SELECT DISTINCT u FROM Utilisateur u
    LEFT JOIN u.roles r
    WHERE (:search IS NULL OR :search = ''
        OR LOWER(u.nomComplet) LIKE LOWER(CONCAT('%', :search, '%'))
        OR LOWER(u.mail)       LIKE LOWER(CONCAT('%', :search, '%'))
        OR LOWER(u.telephone)  LIKE LOWER(CONCAT('%', :search, '%'))
        OR LOWER(r.roles)      LIKE LOWER(CONCAT('%', :search, '%'))
    )
    AND (:actif IS NULL OR u.actif = :actif)
    ORDER BY u.createAt DESC
""")
    List<Utilisateur> searchUsers(
            @Param("search") String search,
            @Param("actif") Boolean actif
    );


    // Plus récent
    @Query("SELECT u FROM Utilisateur u ORDER BY u.createAt DESC")
    List<Utilisateur> findUsersByRecent();

    // A → Z
    @Query("SELECT u FROM Utilisateur u ORDER BY u.nomComplet ASC")
    List<Utilisateur> findUsersByNameAsc();

    // Z → A
    @Query("SELECT u FROM Utilisateur u ORDER BY u.nomComplet DESC")
    List<Utilisateur> findUsersByNameDesc();

    long countByActifTrue();
}
