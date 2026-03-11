package egate.digital.fasotour.model;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "circuit")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Circuit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "circuitName")
    private String circuitName;

    @Column(name = "dateDebut")
    private Date dateDebut;
    private Date dateFin;
    private String description;
    private Duration duree;
    private Double prixIndividuel;
    private int nombreMini;
    private int nombreMax;
    private int nombreExact;
    private LocalDateTime createdAt;
    private String statut;
    private boolean transport = false;
    //Add attribut
    private String lieuRassemblement;
    private LocalTime heureDepart;
    private LocalDateTime dateLimiteReservation;

    @OneToMany(mappedBy = "circuit", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonIgnore
    private Set<Avis> avis = new HashSet<>();

    @OneToMany(mappedBy = "circuit", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonIgnore
    private Set<Reservation> reservations = new HashSet<>();

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "circuit_guide",
            joinColumns = @JoinColumn(name = "circuit_id"),
            inverseJoinColumns = @JoinColumn(name = "guide_id")
    )
    private List<Guide> guides = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "agence_id", nullable = true)
    private Agence agence;

    @ManyToMany()
    @JoinTable(
            name = "circuit_site",
            joinColumns = @JoinColumn(name = "circuit_id"),
            inverseJoinColumns = @JoinColumn(name = "site_id")
    )
    private Set<SiteTouristique> sites = new HashSet<>();

    @PrePersist
    public void prePersist() {
        if (this.createdAt == null) {
            this.createdAt = LocalDateTime.now();
        }
    }
}