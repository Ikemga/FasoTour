package egate.digital.fasotour.security.refreshtoken;

import lombok.Getter;

@Getter
public class TokenException extends RuntimeException{

    private final String errorCode;

    public TokenException(String errorCode, String message) {
        super(message);
        this.errorCode = errorCode;
    }

}
