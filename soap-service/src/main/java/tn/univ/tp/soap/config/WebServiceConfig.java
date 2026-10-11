package tn.univ.tp.soap.config;

import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.ws.config.annotation.EnableWs;
import org.springframework.ws.transport.http.MessageDispatcherServlet;
import org.springframework.ws.wsdl.wsdl11.DefaultWsdl11Definition;
import org.springframework.xml.xsd.SimpleXsdSchema;
import org.springframework.xml.xsd.XsdSchema;

@Configuration
@EnableWs
public class WebServiceConfig {

    public static final String NAMESPACE_URI = "http://tp.univ.tn/personnes";

    /** Schéma XSD, publié dans la section <types> du WSDL généré. */
    @Bean
    public XsdSchema personnesSchema() {
        return new SimpleXsdSchema(new ClassPathResource("xsd/personnes.xsd"));
    }

    /**
     * WSDL généré automatiquement depuis le XSD. Le nom du bean détermine l'URL :
     * http://localhost:8081/ws/personnes.wsdl
     */
    @Bean(name = "personnes")
    public DefaultWsdl11Definition defaultWsdlDefinition(XsdSchema personnesSchema) {
        DefaultWsdl11Definition definition = new DefaultWsdl11Definition();
        definition.setSchema(personnesSchema);
        definition.setTargetNamespace(NAMESPACE_URI);
        definition.setPortTypeName("PersonnesPort");
        definition.setLocationUri("http://localhost:8081/ws");
        return definition;
    }

    @Bean
    public ServletRegistrationBean<MessageDispatcherServlet> messageDispatcherServlet(
            ApplicationContext applicationContext) {
        MessageDispatcherServlet servlet = new MessageDispatcherServlet();
        servlet.setApplicationContext(applicationContext);
        servlet.setTransformWsdlLocations(false);
        return new ServletRegistrationBean<>(servlet, "/ws/*");
    }
}
