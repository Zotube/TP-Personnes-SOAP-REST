package tn.univ.tp.rest.exception;

public class PersonneNotFoundException extends RuntimeException {

    private final Long id;

    public PersonneNotFoundException(Long id) {
        super("Personne introuvable avec l'id " + id);
        this.id = id;
    }

    public PersonneNotFoundException(Long id, String message) {
        super(message);
        this.id = id;
    }

    public PersonneNotFoundException(Long id, String message, Throwable cause) {
        super(message, cause);
        this.id = id;
    }

    public Long getId() {
        return id;
    }
}
