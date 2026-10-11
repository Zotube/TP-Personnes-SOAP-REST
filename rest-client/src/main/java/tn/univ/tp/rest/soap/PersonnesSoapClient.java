package tn.univ.tp.rest.soap;

import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.ws.client.WebServiceFaultException;
import org.springframework.ws.client.core.WebServiceMessageCallback;
import org.springframework.ws.client.core.support.WebServiceGatewaySupport;
import org.springframework.ws.soap.client.SoapFaultClientException;

import jakarta.xml.bind.JAXBElement;
import tn.univ.tp.rest.exception.PersonneNotFoundException;
import tn.univ.tp.rest.exception.SoapCommunicationException;
import tn.univ.tp.rest.generated.AddPersonneRequest;
import tn.univ.tp.rest.generated.AddPersonneResponse;
import tn.univ.tp.rest.generated.DeletePersonneRequest;
import tn.univ.tp.rest.generated.DeletePersonneResponse;
import tn.univ.tp.rest.generated.GetAllPersonnesRequest;
import tn.univ.tp.rest.generated.GetAllPersonnesResponse;
import tn.univ.tp.rest.generated.GetPersonneRequest;
import tn.univ.tp.rest.generated.GetPersonneResponse;
import tn.univ.tp.rest.generated.PersonneType;
import tn.univ.tp.rest.generated.UpdatePersonneRequest;
import tn.univ.tp.rest.generated.UpdatePersonneResponse;

public class PersonnesSoapClient extends WebServiceGatewaySupport {

    private static final Pattern ID_PATTERN = Pattern.compile("(\\d+)");

    private WebServiceMessageCallback requestCallback;

    public void setRequestCallback(WebServiceMessageCallback requestCallback) {
        this.requestCallback = requestCallback;
    }

    public AddPersonneResponse addPersonne(PersonneType personne) {
        AddPersonneRequest request = new AddPersonneRequest();
        request.setPersonne(personne);
        return invoke(request, AddPersonneResponse.class);
    }

    public GetPersonneResponse getPersonne(Long id) {
        GetPersonneRequest request = new GetPersonneRequest();
        request.setId(id);
        return invoke(request, GetPersonneResponse.class);
    }

    public GetAllPersonnesResponse getAllPersonnes() {
        return invoke(new GetAllPersonnesRequest(), GetAllPersonnesResponse.class);
    }

    public UpdatePersonneResponse updatePersonne(PersonneType personne) {
        UpdatePersonneRequest request = new UpdatePersonneRequest();
        request.setPersonne(personne);
        return invoke(request, UpdatePersonneResponse.class);
    }

    public DeletePersonneResponse deletePersonne(Long id) {
        DeletePersonneRequest request = new DeletePersonneRequest();
        request.setId(id);
        return invoke(request, DeletePersonneResponse.class);
    }

    private <T> T invoke(Object request, Class<T> responseType) {
        Object response;
        try {
            if (requestCallback == null) {
                response = getWebServiceTemplate().marshalSendAndReceive(request);
            } else {
                response = getWebServiceTemplate()
                        .marshalSendAndReceive(request, requestCallback);
            }
        } catch (SoapFaultClientException ex) {
            throw translateFault(ex.getFaultStringOrReason(), ex);
        } catch (WebServiceFaultException ex) {
            throw translateFault(ex.getMessage(), ex);
        } catch (RuntimeException ex) {
            throw new SoapCommunicationException(
                    "Impossible de joindre le service SOAP sur " + getDefaultUri(), ex);
        }

        if (response instanceof JAXBElement<?> element) {
            response = element.getValue();
        }
        if (response == null) {
            throw new SoapCommunicationException(
                    "Reponse SOAP vide recue de " + getDefaultUri());
        }
        return responseType.cast(response);
    }

    private RuntimeException translateFault(String reason, Throwable cause) {
        String text = reason == null ? "" : reason.trim();
        String lower = text.toLowerCase(Locale.ROOT);
        if (lower.contains("introuvable") || lower.contains("notfoundexception")
                || lower.contains("not found")) {
            return new PersonneNotFoundException(extractId(text), text, cause);
        }
        return new SoapCommunicationException("Le service SOAP a renvoye une faute : " + text, cause);
    }

    private Long extractId(String text) {
        Matcher matcher = ID_PATTERN.matcher(text);
        Long id = null;
        while (matcher.find()) {
            try {
                id = Long.valueOf(matcher.group(1));
            } catch (NumberFormatException ignored) {
                id = null;
            }
        }
        return id;
    }
}
