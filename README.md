# TP Personnes — Service SOAP et API REST

## 1. Présentation du projet

Ce projet consiste à développer deux applications Spring Boot qui communiquent entre elles :

- **soap-service** : expose un service web SOAP permettant de gérer les personnes.
- **rest-client** : expose une API REST qui consomme le service SOAP et permet aux clients HTTP d'effectuer les opérations CRUD.

Les données sont gérées par le service SOAP et stockées dans une base de données H2. Le client REST ne possède pas sa propre base de données.

## 2. Architecture

```text
Client HTTP (Postman, navigateur, etc.)
                 |
                 | HTTP / REST
                 v
        API REST — Port 8080
            rest-client
                 |
                 | SOAP / XML
                 v
       Service SOAP — Port 8081
            soap-service
                 |
                 v
             Base H2
```

## 3. Technologies utilisées

- Java
- Spring Boot
- Spring Web
- Spring Web Services (SOAP)
- API REST
- JAXB pour les classes XML générées à partir du contrat WSDL/XSD
- Maven
- Base de données H2
- Git et GitHub

## 4. Structure du dépôt

```text
TP-Personnes-SOAP-REST/
├── soap-service/
│   ├── src/
│   ├── pom.xml
│   └── README.md
├── rest-client/
│   ├── src/
│   ├── pom.xml
│   └── src/main/resources/wsdl/
└── README.md
```

## 5. Prérequis

- JDK 17
- Apache Maven
- Git
- Un terminal Windows ou un terminal compatible

Vérifier les installations :

```bash
java -version
mvn -version
git --version
```

## 6. Démarrer le service SOAP

Ouvrir un premier terminal à la racine du dépôt :

```bash
cd soap-service
mvn spring-boot:run
```

Le service SOAP est configuré pour démarrer sur le port 8081.

Le WSDL est accessible à l'adresse :

http://localhost:8081/ws/personnes.wsdl

## 7. Démarrer le client REST

Ouvrir un deuxième terminal à la racine du dépôt :

```bash
cd rest-client
mvn spring-boot:run
```

Par défaut, l'API REST est configurée sur le port 8080. Si ce port est déjà utilisé, démarrer temporairement l'application sur le port 8082 :

```bash
mvn spring-boot:run -Dspring-boot.run.arguments=--server.port=8082
```

Dans ce cas, remplacer le port 8080 par 8082 dans les URL des requêtes REST.

## 8. Endpoints REST

| Méthode | Endpoint | Description |
|---|---|---|
| GET | `/api/personnes` | Lister toutes les personnes |
| GET | `/api/personnes/{id}` | Consulter une personne |
| POST | `/api/personnes` | Ajouter une personne |
| PUT | `/api/personnes/{id}` | Modifier une personne |
| DELETE | `/api/personnes/{id}` | Supprimer une personne |

Exemple de corps JSON pour créer une personne :

```json
{
  "nom": "Diop",
  "prenom": "Awa",
  "age": 25
}
```

Exemple de requête :

```http
POST http://localhost:8082/api/personnes
Content-Type: application/json
```

L'exemple utilise le port 8082, correspondant au démarrage alternatif du client REST.

## 9. Opérations SOAP

Le service SOAP expose les opérations suivantes :

- `addPersonne`
- `getPersonne`
- `getAllPersonnes`
- `updatePersonne`
- `deletePersonne`

Le contrat WSDL décrit les opérations disponibles et les structures XML utilisées pour les échanges.

## 10. Tests réalisés

Les tests manuels de l'API REST ont vérifié :

- La récupération de la liste des personnes.
- La création d'une personne.
- La consultation d'une personne par identifiant.
- La modification d'une personne.
- La suppression d'une personne.
- Le retour d'une erreur HTTP 404 pour un identifiant inexistant.

## 11. Auteur

Master 1 Génie Logiciel.