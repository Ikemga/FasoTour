package egate.digital.fasotour.model;

import java.time.LocalDate;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
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
@Table(name = "avis")
public class Avis {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    
    @Column(name = "avis")
    private String avis;

    @Column(name = "note")
    private Long note;

    @Column(name = "date")
    private LocalDate date;

    /**
     * Relatin acvec circuit
    */
    @ManyToOne
    @JoinColumn(name= "circuit_id")
    private Circuit circuit;

    /**
     * Relatin acvec Guide
    */
    @ManyToOne
    @JoinColumn(name= "guide_id")
    private Guide guide;

    /**
     * Relatin acvec Touriste
    */
    @OneToOne(optional=false, fetch = FetchType.LAZY)
    @JoinColumn(name = "touriste_id", unique = true, nullable=false)
    private Touriste touriste;
}