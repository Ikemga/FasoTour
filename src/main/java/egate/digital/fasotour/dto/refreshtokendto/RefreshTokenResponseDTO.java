package egate.digital.fasotour.dto.refreshtokendto;

public record RefreshTokenResponseDTO(
        String accessToken,
        String refreshToken
) {
}
