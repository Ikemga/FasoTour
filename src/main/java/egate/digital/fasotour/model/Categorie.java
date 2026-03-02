
package egate.digital.fasotour.model;

import java.util.List;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * @author Charles
 */
@Entity
@Data
@EqualsAndHashCode(exclude = "siteTouristique")
@AllArgsConstructor
@NoArgsConstructor
@Table(name="categarie")
public class Categorie {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name="categorieName", nullable = false, unique = true)
    private String categorie;

    private String description;

    private String statut;

    @JsonIgnore
    @ManyToMany(mappedBy = "categories",fetch=FetchType.LAZY)
    
    private Set<SiteTouristique> siteTouristique;
}