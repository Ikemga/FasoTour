package egate.digital.fasotour.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "touristes")
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
public class Touriste extends Utilisateur {
    /**
 * @author Charles
 */

    private String preferenceTouristique;

     /**
     * Relatio Réservation
    */
   @OneToMany(mappedBy="touriste",cascade = CascadeType.ALL, fetch = FetchType.LAZY)
   @JsonIgnore
   private List<Reservation> reservations;

    /**
     * Relation avec avis
    */
    @OneToOne(mappedBy = "touriste")
    @JsonIgnore
    private Avis avis;
}
