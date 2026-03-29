package egate.digital.fasotour.model;

import java.time.LocalDate;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
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
@Table(name = "reservation")
public class Reservation {

    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private Long id;
    private int nombrePersonne;

    private Double prixCircuit;
    private Double fraisReservation;
    private Double montantTotal;

    private String commentaire;
    private String reference;
    private String statut;

    private LocalDate dateResevation = LocalDate.now();
    private LocalDate dateLimitePaiement;

    /**
     * Relatin qvec paiement
    */

    @OneToOne(mappedBy = "reservation", cascade = CascadeType.ALL)
    private Paiement paiement;

     /**
     * Relatin acvec circuit
    */
    @ManyToOne
    @JoinColumn(name= "circuit_id")
    private Circuit circuit;

    /**
     * Relatin acvec touriste
    */
    @ManyToOne
    @JoinColumn(name= "touriste_id")
    private Touriste touriste;

}