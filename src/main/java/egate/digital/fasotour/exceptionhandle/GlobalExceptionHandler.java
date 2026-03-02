package egate.digital.fasotour.exceptionhandle;

import egate.digital.fasotour.dto.ErrorEntity;
import egate.digital.fasotour.dto.ErrorResponse;
import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.stream.Collectors;


@RestControllerAdvice
public class GlobalExceptionHandler {

    // 404 - Entity non trouvée
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler({EntityNotFoundException.class})
    public ErrorEntity handeleException(EntityNotFoundException exception) {
        return new ErrorEntity (null, exception.getMessage());
    }

    // 400 - RuntimeException métier
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler({RuntimeException.class})
    public ErrorResponse handleRuntime(
            RuntimeException exception, HttpServletRequest request) {

        return new ErrorResponse(null, exception.getMessage(), request.getRequestURI());
    }

    //401 - Mauvais identifiants
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ExceptionHandler({BadCredentialsException.class})
    public ErrorResponse handleBadCredentials(
            BadCredentialsException exception, HttpServletRequest request) {

        return new ErrorResponse(null, exception.getMessage(), request.getRequestURI());
    }

    // 400 - Validation échouée
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler({MethodArgumentNotValidException.class})
    public  ErrorResponse handleException (MethodArgumentNotValidException exception, HttpServletRequest request){
        String message = exception.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(e -> e.getField() + " : " + e.getDefaultMessage())
                .collect(Collectors.joining(", "));
        return new ErrorResponse(null,exception.getMessage(),request.getRequestURI());
    }

    // 403 - Accès refusé
    @ResponseStatus(HttpStatus.FORBIDDEN)
    @ExceptionHandler({AccessDeniedException.class})
    public ErrorResponse handleException(AccessDeniedException exception, HttpServletRequest request){
        return new ErrorResponse(null, exception.getMessage(),request.getRequestURI());
    }

    // 500 - Erreur générale
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler({Exception.class})
    public ErrorResponse handleException(Exception exception, HttpServletRequest request){
        return new ErrorResponse(null, exception.getMessage(), request.getRequestURI());
    }
}