package tn.univ.tp.rest.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;
import org.springframework.ws.client.core.WebServiceTemplate;
import org.springframework.ws.soap.client.core.SoapActionCallback;

import tn.univ.tp.rest.soap.PersonnesSoapClient;

@Configuration
public class SoapClientConfig {

    public static final String NAMESPACE_URI = "http://tp.univ.tn/personnes";

    public static final String JAXB_CONTEXT_PATH = "tn.univ.tp.rest.generated";

    @Value("${soap.client.url}")
    private String personnesUrl;

    @Bean
    public Jaxb2Marshaller jaxb2Marshaller() {
        Jaxb2Marshaller marshaller = new Jaxb2Marshaller();
        marshaller.setContextPath(JAXB_CONTEXT_PATH);
        return marshaller;
    }

    @Bean
    public WebServiceTemplate webServiceTemplate(Jaxb2Marshaller marshaller) {
        WebServiceTemplate template = new WebServiceTemplate();
        template.setMarshaller(marshaller);
        template.setUnmarshaller(marshaller);
        template.setDefaultUri(personnesUrl);
        template.setCheckConnectionForError(true);
        template.setCheckConnectionForFault(true);
        return template;
    }

    @Bean
    public PersonnesSoapClient personnesSoapClient(WebServiceTemplate webServiceTemplate) {
        PersonnesSoapClient client = new PersonnesSoapClient();
        client.setWebServiceTemplate(webServiceTemplate);
        client.setRequestCallback(new SoapActionCallback(""));
        return client;
    }
}
