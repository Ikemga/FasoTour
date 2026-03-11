
package egate.digital.fasotour.model;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;

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

    @CreatedDate
    @Column(updatable = false)
    private LocalDateTime createdAt;
    /*
    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }
    */

    @JsonIgnore
    @ManyToMany(mappedBy = "categories",fetch=FetchType.LAZY)
    
    private Set<SiteTouristique> siteTouristique;
}