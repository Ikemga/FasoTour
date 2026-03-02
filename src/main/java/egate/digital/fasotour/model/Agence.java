package egate.digital.fasotour.model;

import java.util.Date;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "agences")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Agence extends Utilisateur {

    private String sitWeb;
    private String pageFacebook;
    private String numeroAgrement;
    private Date dateCreation;

    /*
     * Relation avec Circuit
     */
    @OneToMany(mappedBy = "agence", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonIgnore
    private Set<Circuit> circuits;

    /*
     * Relation avec Guide
     */
    @ManyToMany
    @JoinTable(
        name = "agence_guide",
        joinColumns = @JoinColumn(name = "agence_id"),
        inverseJoinColumns = @JoinColumn(name = "guide_id")
    )
    private Set<Guide> guides;
}