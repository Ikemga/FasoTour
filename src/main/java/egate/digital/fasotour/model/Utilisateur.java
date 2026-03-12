package egate.digital.fasotour.model;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

import com.fasterxml.jackson.annotation.JsonIgnore;
import egate.digital.fasotour.security.refreshtoken.RefreshToken;
import jakarta.persistence.*;
import java.time.Instant;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import egate.digital.fasotour.model.valide.Validate;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "utilisateurs")
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Inheritance(strategy = InheritanceType.JOINED)

public abstract class Utilisateur implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nomComplet;
    private String adresse;

    @Column(unique = true)
    private String mail;

    private String telephone;

    private String motDePasse;

    private LocalDateTime createAt = LocalDateTime.now();

    private String pays;
    private String photo;

    @Column(length = 1000)
    private String bio;

    private Boolean actif = true;

    @Column(nullable = false)
    private int failedLoginAttempts = 0;

    @Column
    private Instant lockedUntil;
    /*
     * Relation avec Langue
     */
    @ManyToMany
    @JoinTable(
        name = "utilisateur_langue",
        joinColumns = @JoinColumn(name = "utilisateur_id"),
        inverseJoinColumns = @JoinColumn(name = "langue_id")
    )
    private Set<Langue> langues = new HashSet<>();

    /*
     * Relation avec Role
     */
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "utilisateur_role",
            joinColumns = @JoinColumn(name = "utilisateur_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id")
    )

    private Set<Role> roles = new HashSet<>();

    //
    @OneToMany(mappedBy = "utilisateur",
            cascade = CascadeType.ALL,
            orphanRemoval = true)
    private List<Validate> valide = new ArrayList<>();
    //

    // Re
    // Dans Utilisateur.java
    @OneToOne(mappedBy = "utilisateur", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private RefreshToken refreshToken;


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Optional.ofNullable(roles)
                .orElse(new HashSet<>())
                .stream()
                .map(role -> new SimpleGrantedAuthority(role.getRoles()))
                .collect(Collectors.toList());
    }

    @Override
    public String getPassword() {
        return this.motDePasse;
    }

    @Override
    public String getUsername() {
        return this.mail;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return !isLockedOut();
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return this.actif;
    }

    public boolean isLockedOut() {
        return lockedUntil != null && Instant.now().isBefore(lockedUntil);
    }

}