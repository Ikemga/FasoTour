package egate.digital.fasotour.dto.auth;

public record LoginRequestDTO(
        String mail,
        String motDePasse
) {}