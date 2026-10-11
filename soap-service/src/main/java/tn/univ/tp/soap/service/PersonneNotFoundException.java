package tn.univ.tp.soap.service;

public class PersonneNotFoundException extends RuntimeException {

    public PersonneNotFoundException(Long id) {
        super("Personne introuvable avec l'id " + id);
    }
}
