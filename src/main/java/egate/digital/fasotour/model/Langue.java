package egate.digital.fasotour.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Charles
 */

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "langue")
public class Langue {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "Languename")
    private String langues;

    private String code;

    private String region;

    @JsonIgnore
    @ManyToMany(mappedBy = "langues")
    private List<Utilisateur> utilisateurs;
}