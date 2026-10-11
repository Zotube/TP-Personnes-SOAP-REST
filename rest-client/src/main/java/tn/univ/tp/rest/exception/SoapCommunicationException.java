package tn.univ.tp.rest.exception;

public class SoapCommunicationException extends RuntimeException {

    public SoapCommunicationException(String message) {
        super(message);
    }

    public SoapCommunicationException(String message, Throwable cause) {
        super(message, cause);
    }
}
