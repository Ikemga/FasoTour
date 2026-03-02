package egate.digital.fasotour.model;

import java.util.Date;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
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
@Table(name = "paiement")
public class Paiement {
    
    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private Long id;

    private Date datePaiement;

    private Double montant;

    private String reference;

    private String statut;

    private Double montantPaye;

    /**
     * Relation avec Facture (1-1)
     */
    @OneToOne(fetch= FetchType.LAZY, cascade=CascadeType.ALL)
    @JoinColumn(name = "facture_id")
    private Facture facture;

    /**
     * Relation avec Réservation (1-1)
     */
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reservation_id")
    private Reservation reservation;
}