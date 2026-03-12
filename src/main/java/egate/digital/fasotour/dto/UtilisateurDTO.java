package egate.digital.fasotour.dto;

import lombok.Data;
import java.time.LocalDateTime;
import java.util.Set;

@Data
public class UtilisateurDTO {
    private Long id;
    private String nomComplet;
    private String mail;
    private String telephone;
    private Boolean actif;
    private String adresse;
    private String pays;
    private String photo;
    private String bio;

    private LocalDateTime createAt;
    private Set<String> roles;
}