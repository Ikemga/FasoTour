package egate.digital.fasotour.model;

import java.util.*;

import jakarta.persistence.*;
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
@Table(name = "facture")
public class Facture {

    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private Long id;

    private String reference;

    private Date dateEmission;

    private Double montatTotal;

    private Double montantPaie;

    private Double montantRestant;

    @OneToOne(mappedBy = "facture", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Paiement paiement;

}