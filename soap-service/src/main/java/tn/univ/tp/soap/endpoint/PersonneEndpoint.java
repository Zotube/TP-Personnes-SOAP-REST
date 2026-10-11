package tn.univ.tp.soap.endpoint;

import java.util.List;

import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;
import org.springframework.ws.server.endpoint.annotation.RequestPayload;
import org.springframework.ws.server.endpoint.annotation.ResponsePayload;

import tn.univ.tp.soap.config.WebServiceConfig;
import tn.univ.tp.soap.entity.Personne;
import tn.univ.tp.soap.generated.AddPersonneRequest;
import tn.univ.tp.soap.generated.AddPersonneResponse;
import tn.univ.tp.soap.generated.DeletePersonneRequest;
import tn.univ.tp.soap.generated.DeletePersonneResponse;
import tn.univ.tp.soap.generated.GetAllPersonnesRequest;
import tn.univ.tp.soap.generated.GetAllPersonnesResponse;
import tn.univ.tp.soap.generated.GetPersonneRequest;
import tn.univ.tp.soap.generated.GetPersonneResponse;
import tn.univ.tp.soap.generated.PersonneType;
import tn.univ.tp.soap.generated.UpdatePersonneRequest;
import tn.univ.tp.soap.generated.UpdatePersonneResponse;
import tn.univ.tp.soap.service.PersonneService;

@Endpoint
public class PersonneEndpoint {

    private static final String NS = WebServiceConfig.NAMESPACE_URI;

    private final PersonneService personneService;

    public PersonneEndpoint(PersonneService personneService) {
        this.personneService = personneService;
    }

    @PayloadRoot(namespace = NS, localPart = "addPersonneRequest")
    @ResponsePayload
    public AddPersonneResponse addPersonne(@RequestPayload AddPersonneRequest request) {
        Personne saved = personneService.ajouter(toEntity(request.getPersonne()));
        AddPersonneResponse response = new AddPersonneResponse();
        response.setPersonne(toType(saved));
        return response;
    }

    @PayloadRoot(namespace = NS, localPart = "getPersonneRequest")
    @ResponsePayload
    public GetPersonneResponse getPersonne(@RequestPayload GetPersonneRequest request) {
        Personne personne = personneService.getById(request.getId());
        GetPersonneResponse response = new GetPersonneResponse();
        response.setPersonne(toType(personne));
        return response;
    }

    @PayloadRoot(namespace = NS, localPart = "getAllPersonnesRequest")
    @ResponsePayload
    public GetAllPersonnesResponse getAllPersonnes(@RequestPayload GetAllPersonnesRequest request) {
        List<Personne> personnes = personneService.getAll();
        GetAllPersonnesResponse response = new GetAllPersonnesResponse();
        for (Personne personne : personnes) {
            response.getPersonne().add(toType(personne));
        }
        return response;
    }

    @PayloadRoot(namespace = NS, localPart = "updatePersonneRequest")
    @ResponsePayload
    public UpdatePersonneResponse updatePersonne(@RequestPayload UpdatePersonneRequest request) {
        Personne updated = personneService.modifier(toEntity(request.getPersonne()));
        UpdatePersonneResponse response = new UpdatePersonneResponse();
        response.setPersonne(toType(updated));
        return response;
    }

    @PayloadRoot(namespace = NS, localPart = "deletePersonneRequest")
    @ResponsePayload
    public DeletePersonneResponse deletePersonne(@RequestPayload DeletePersonneRequest request) {
        DeletePersonneResponse response = new DeletePersonneResponse();
        response.setSuccess(personneService.supprimer(request.getId()));
        return response;
    }

    private Personne toEntity(PersonneType type) {
        Personne personne = new Personne();
        personne.setId(type.getId());
        personne.setNom(type.getNom());
        personne.setPrenom(type.getPrenom());
        personne.setAge(type.getAge());
        return personne;
    }

    private PersonneType toType(Personne personne) {
        PersonneType type = new PersonneType();
        type.setId(personne.getId());
        type.setNom(personne.getNom());
        type.setPrenom(personne.getPrenom());
        type.setAge(personne.getAge());
        return type;
    }
}
