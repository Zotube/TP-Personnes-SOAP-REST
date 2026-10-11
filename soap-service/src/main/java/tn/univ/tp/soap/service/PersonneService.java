package tn.univ.tp.soap.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import tn.univ.tp.soap.entity.Personne;
import tn.univ.tp.soap.repository.PersonneRepository;

@Service
@Transactional
public class PersonneService {

    private final PersonneRepository repository;

    public PersonneService(PersonneRepository repository) {
        this.repository = repository;
    }

    public Personne ajouter(Personne personne) {
        return repository.save(personne);
    }

    @Transactional(readOnly = true)
    public Personne getById(Long id) {
        return repository.findById(id).orElseThrow(() -> new PersonneNotFoundException(id));
    }

    @Transactional(readOnly = true)
    public List<Personne> getAll() {
        return repository.findAll();
    }

    public Personne modifier(Personne personne) {
        if (!repository.existsById(personne.getId())) {
            throw new PersonneNotFoundException(personne.getId());
        }
        return repository.save(personne);
    }

    public boolean supprimer(Long id) {
        if (!repository.existsById(id)) {
            throw new PersonneNotFoundException(id);
        }
        repository.deleteById(id);
        return true;
    }
}
