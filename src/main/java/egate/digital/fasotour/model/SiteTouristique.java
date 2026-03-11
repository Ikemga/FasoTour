package egate.digital.fasotour.model;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

/**
 * @author Charles
 */
@Entity
@Data
@EqualsAndHashCode(exclude = "categories")
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "siteTouristique")
public class SiteTouristique {
    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private Long id;

    @Column(name ="SiteName")
    private String nom;

    private String description;

    private String region;

    private String noteMoyenne;

    private String image;

    private String video;

    private String fichier;

    // Add Attribut
    private LocalTime horaire;
    private Double tarif;
    private String statut = "Actif";

    @CreationTimestamp
    private LocalDateTime createdAt = LocalDateTime.now();

    private String localisation;

    /*
     * Relation avec categorie
     */

    @ManyToMany
    @JoinTable(
        name = "site_categorie",
        joinColumns = @JoinColumn(name = "site_id"),
        inverseJoinColumns = @JoinColumn(name = "categorie_id")
    )
    private Set<Categorie> categories;

    /*
     * Relation avec Circuit
     */
     @ManyToMany(mappedBy = "sites")
    @JsonIgnore
    private Set<Circuit> circuits;

}