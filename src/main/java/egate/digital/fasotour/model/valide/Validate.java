package egate.digital.fasotour.model.valide;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

import egate.digital.fasotour.model.Utilisateur;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name= "validation")
public class Validate {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private Instant created;
    private Instant expired;
    private Instant activated;
    private String code;

    // Relatio

    @OneToOne(cascade = CascadeType.ALL)
    private Utilisateur utilisateur;

}
