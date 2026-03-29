package egate.digital.fasotour.model;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "guides")
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
public class
Guide extends Utilisateur {

    private String experience;

    private String preferenceTouristique;


    /**
     * Relation Avis
    */
   @OneToMany(mappedBy="guide", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
   @JsonIgnore
   private List<Avis> avis;

   /**
    * Relation  avec circuit
   */
   @OneToMany(mappedBy="guides")
   @JsonIgnore
   private Set<Circuit> circuit =  new HashSet<>();

    /**
    * Relation  avec Agence
   */
    @ManyToMany(mappedBy = "guides")
    @JsonIgnore
    private Set<Agence> agences = new HashSet<>();
}
