package egate.digital.fasotour.dto;

public record ErrorResponse(
        String status,
        String message,
        String path

) {
}
