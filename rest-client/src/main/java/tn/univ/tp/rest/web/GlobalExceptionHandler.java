package tn.univ.tp.rest.web;

import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import tn.univ.tp.rest.exception.InvalidPersonneException;
import tn.univ.tp.rest.exception.PersonneNotFoundException;
import tn.univ.tp.rest.exception.SoapCommunicationException;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(PersonneNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ApiError handleNotFound(PersonneNotFoundException ex) {
        return ApiError.of(404, "Not Found", ex.getMessage());
    }

    @ExceptionHandler(InvalidPersonneException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiError handleInvalidData(InvalidPersonneException ex) {
        return ApiError.of(400, "Bad Request", ex.getMessage());
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiError handleUnreadableBody(HttpMessageNotReadableException ex) {
        return ApiError.of(400, "Bad Request",
                "Corps JSON invalide ou mal forme : " + ex.getMostSpecificCause().getMessage());
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiError handleTypeMismatch(MethodArgumentTypeMismatchException ex) {
        return ApiError.of(400, "Bad Request",
                "Valeur invalide pour le parametre '" + ex.getName() + "' : " + ex.getValue());
    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiError handleMissingParameter(MissingServletRequestParameterException ex) {
        return ApiError.of(400, "Bad Request", "Parametre manquant : " + ex.getParameterName());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiError handleValidation(MethodArgumentNotValidException ex) {
        String message = ex.getBindingResult().getFieldErrors().stream()
                .map(error -> error.getField() + " : " + error.getDefaultMessage())
                .collect(Collectors.joining(", "));
        return ApiError.of(400, "Bad Request", message);
    }

    @ExceptionHandler(SoapCommunicationException.class)
    @ResponseStatus(HttpStatus.BAD_GATEWAY)
    public ApiError handleSoapCommunication(SoapCommunicationException ex) {
        return ApiError.of(502, "Bad Gateway", ex.getMessage());
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ApiError handleUnexpected(Exception ex) {
        return ApiError.of(500, "Internal Server Error",
                "Erreur inattendue : " + ex.getMessage());
    }
}
