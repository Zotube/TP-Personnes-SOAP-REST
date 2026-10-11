# soap-service — API SOAP CRUD Personne (Spring Boot)

Service **SOAP** (Spring Web Services) exposant un CRUD `Personne` (id, nom, prenom, age),
persisté en mémoire via **Spring Data JPA + H2**. Aucun contrôleur REST dans ce projet :
l'accès se fait uniquement via le point d'entrée SOAP.

- Port HTTP : **8081**
- WSDL : `http://localhost:8081/ws/personnes.wsdl`
- Base : H2 **en mémoire** (`jdbc:h2:mem:personnes`), aucune base MySQL/PostgreSQL.
- Java 17+, Spring Boot 3.5.16.

## Prérequis

- JDK 17 ou supérieur (`JAVA_HOME` doit pointer dessus ; Spring Boot 3 est incompatible avec Java 8/11)
- Maven 3.6+

## Compilation

```powershell
mvn clean verify
```

Le plugin **jaxb-maven-plugin** (`org.jvnet.jaxb:jaxb-maven-plugin:4.0.16`) exécute `xjc`
sur `src/main/resources/xsd/personnes.xsd` et génère les classes JAXB dans
`target/generated-sources/xjc` (package `tn.univ.tp.soap.generated`) avant la compilation.

## Lancement

```powershell
mvn spring-boot:run
```

ou, après `mvn clean package` :

```powershell
java -jar target/soap-service-1.0.0.jar
```

L'application démarre sur `http://localhost:8081`.

## Vérification du WSDL

```powershell
curl http://localhost:8081/ws/personnes.wsdl
```

ou via PowerShell :

```powershell
Invoke-WebRequest -Uri http://localhost:8081/ws/personnes.wsdl -UseBasicParsing | Select-Object -ExpandProperty Content
```

Le WSDL est généré automatiquement par `DefaultWsdl11Definition` (bean `personnes`) depuis
le XSD ; il expose les 5 opérations document/literal : `addPersonneRequest`,
`getPersonneRequest`, `getAllPersonnesRequest`, `updatePersonneRequest`, `deletePersonneRequest`.

### Exemple d'appel SOAP (addPersonne)

POST `http://localhost:8081/ws` avec l'en-tête `Content-Type: text/xml` :

```xml
<soapenv:Envelope xmlns:soapenv="http://schemas.xmlsoap.org/soap/envelope/"
                  xmlns:tns="http://tp.univ.tn/personnes">
  <soapenv:Header/>
  <soapenv:Body>
    <tns:addPersonneRequest>
      <tns:personne>
        <tns:nom>Ben Ali</tns:nom>
        <tns:prenom>Sami</tns:prenom>
        <tns:age>23</tns:age>
      </tns:personne>
    </tns:addPersonneRequest>
  </soapenv:Body>
</soapenv:Envelope>
```

### Console H2 (données de démonstration)

`http://localhost:8081/h2-console` — JDBC URL : `jdbc:h2:mem:personnes`, utilisateur `sa`, mot de passe vide.

## Architecture

```
src/main/java/tn/univ/tp/soap/
├── SoapServiceApplication.java      # Point d'entrée Spring Boot
├── config/WebServiceConfig.java     # @EnableWs, servlet /ws/*, WSDL /ws/personnes.wsdl
├── endpoint/PersonneEndpoint.java   # @Endpoint : 5 opérations SOAP (mappées sur le XSD)
├── entity/Personne.java             # Entité JPA (id auto-généré, nom, prenom, age)
├── repository/PersonneRepository.java # JpaRepository<Personne, Long>
└── service/
    ├── PersonneService.java         # Logique métier (ajouter, getById, getAll, modifier, supprimer)
    └── PersonneNotFoundException.java # Erreur métier si id inconnu

src/main/resources/
├── application.properties           # Port 8081, H2 en mémoire
└── xsd/personnes.xsd                # Schéma des 5 opérations (source de vérité du contrat)
```

## Opérations exposées

| Opération           | Requête XML racine         | Effet                                   |
|---------------------|----------------------------|-----------------------------------------|
| addPersonne         | `addPersonneRequest`       | Crée une personne, retourne l'id généré |
| getPersonne         | `getPersonneRequest`       | Lit une personne par id                 |
| getAllPersonnes     | `getAllPersonnesRequest`   | Liste toutes les personnes              |
| updatePersonne      | `updatePersonneRequest`    | Met à jour (erreur  si id inconnu)      |
| deletePersonne      | `deletePersonneRequest`    | Supprime, retourne `success=true`       |
