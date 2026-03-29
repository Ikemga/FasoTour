package egate.digital.fasotour.model;

import java.time.Duration;
import java.time.LocalDate;
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
    private LocalDate dateDebut;
    private LocalDate dateFin;
    private String description;

    private Long duree;
    private Double prixIndividuel;
    private Integer nombreExact;
    @Column(nullable = false)
    private Integer nombreRestant = 0;
    private LocalDateTime createdAt;
    private String statut;
    private boolean transport = false;
    //Add attribut
    private String lieuRassemblement;
    private LocalTime heureDepart;
    private LocalDate dateLimiteReservation;
    private String image;

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
    private Set<Guide> guides = new HashSet<>();

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