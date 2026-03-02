package egate.digital.fasotour.dto.auth;

public record AuthResponseDTO(
        String accessToken,
        String refreshToken,
        //String tokenType,
        Long userId,
        String nomComplet,
        String mail,
        java.util.List<String> roles) {}