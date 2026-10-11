package tn.univ.tp.rest.service;

import java.util.List;

import org.springframework.stereotype.Service;

import tn.univ.tp.rest.dto.DeleteResponse;
import tn.univ.tp.rest.dto.PersonneDto;
import tn.univ.tp.rest.dto.PersonneRequest;
import tn.univ.tp.rest.exception.InvalidPersonneException;
import tn.univ.tp.rest.exception.PersonneNotFoundException;
import tn.univ.tp.rest.generated.AddPersonneResponse;
import tn.univ.tp.rest.generated.DeletePersonneResponse;
import tn.univ.tp.rest.generated.GetAllPersonnesResponse;
import tn.univ.tp.rest.generated.GetPersonneResponse;
import tn.univ.tp.rest.generated.PersonneType;
import tn.univ.tp.rest.generated.UpdatePersonneResponse;
import tn.univ.tp.rest.soap.PersonnesSoapClient;

@Service
public class PersonneService {

    private static final int AGE_MIN = 0;
    private static final int AGE_MAX = 150;
    private static final int NOM_MAX = 100;

    private final PersonnesSoapClient soapClient;

    public PersonneService(PersonnesSoapClient soapClient) {
        this.soapClient = soapClient;
    }

    public List<PersonneDto> getAll() {
        GetAllPersonnesResponse response = soapClient.getAllPersonnes();
        return response.getPersonne().stream().map(PersonneService::toDto).toList();
    }

    public PersonneDto getById(Long id) {
        GetPersonneResponse response = soapClient.getPersonne(id);
        return requireFound(response.getPersonne(), id);
    }

    public PersonneDto create(PersonneRequest request) {
        validate(request);
        AddPersonneResponse response = soapClient.addPersonne(toType(null, request));
        PersonneType personne = response.getPersonne();
        if (personne == null) {
            throw new InvalidPersonneException("Le service SOAP n'a pas renvoye de personne creee");
        }
        return toDto(personne);
    }

    public PersonneDto update(Long id, PersonneRequest request) {
        validate(request);
        UpdatePersonneResponse response = soapClient.updatePersonne(toType(id, request));
        return requireFound(response.getPersonne(), id);
    }

    public DeleteResponse delete(Long id) {
        DeletePersonneResponse response = soapClient.deletePersonne(id);
        if (!response.isSuccess()) {
            throw new PersonneNotFoundException(id);
        }
        return new DeleteResponse(id, true);
    }

    private PersonneDto requireFound(PersonneType personne, Long id) {
        if (personne == null || personne.getId() == null) {
            throw new PersonneNotFoundException(id);
        }
        return toDto(personne);
    }

    private void validate(PersonneRequest request) {
        if (request == null) {
            throw new InvalidPersonneException("Corps JSON requis : { nom, prenom, age }");
        }
        if (isBlank(request.nom())) {
            throw new InvalidPersonneException("Le champ 'nom' est obligatoire");
        }
        if (request.nom().length() > NOM_MAX) {
            throw new InvalidPersonneException("Le champ 'nom' ne doit pas depasser "
                    + NOM_MAX + " caracteres");
        }
        if (isBlank(request.prenom())) {
            throw new InvalidPersonneException("Le champ 'prenom' est obligatoire");
        }
        if (request.prenom().length() > NOM_MAX) {
            throw new InvalidPersonneException("Le champ 'prenom' ne doit pas depasser "
                    + NOM_MAX + " caracteres");
        }
        if (request.age() == null) {
            throw new InvalidPersonneException("Le champ 'age' est obligatoire");
        }
        if (request.age() < AGE_MIN || request.age() > AGE_MAX) {
            throw new InvalidPersonneException(
                    "Le champ 'age' doit etre compris entre " + AGE_MIN + " et " + AGE_MAX);
        }
    }

    private PersonneType toType(Long id, PersonneRequest request) {
        PersonneType personne = new PersonneType();
        personne.setId(id);
        personne.setNom(request.nom().trim());
        personne.setPrenom(request.prenom().trim());
        personne.setAge(request.age());
        return personne;
    }

    private static PersonneDto toDto(PersonneType personne) {
        return new PersonneDto(personne.getId(), personne.getNom(), personne.getPrenom(),
                personne.getAge());
    }

    private static boolean isBlank(String value) {
        return value == null || value.isBlank();
    }
}
