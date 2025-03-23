# Yoga Studio - Application Full Stack

Cette application est un projet full-stack pour un studio de yoga, composé d'une partie frontend en Angular et d'une API backend en Java.

## Architecture du projet

Le projet est organisé en deux parties principales :
- **/front** : Application frontend développée avec Angular CLI v14.1.0
- **/back** : API backend développée en Java avec Maven

## Prérequis

- Node.js et npm (pour le frontend)
- Java 8+ et Maven (pour le backend)
- MySQL (pour la base de données)

## Installation et démarrage du projet

### 1. Cloner le projet

```bash
git clone https://github.com/OpenClassrooms-Student-Center/P5-Full-Stack-testing
cd P5-Full-Stack-testing
```

### 2. Configuration de la base de données

- Créez une base de données MySQL
- Exécutez le script SQL situé dans `ressources/sql/script.sql`

Par défaut, le compte administrateur est :
- login: yoga@studio.com
- password: test!1234

### 3. Démarrer le backend

```bash
cd back
mvn spring-boot:run
```

### 4. Démarrer le frontend

```bash
cd front
npm install
npm run start
```

L'application sera disponible à l'adresse : http://localhost:4200

## Ressources

### Mockoon

Un environnement Mockoon est disponible dans le dossier `ressources/mockoon`.

### Postman

Une collection Postman est disponible dans :
```
ressources/postman/yoga.postman_collection.json
```

Pour l'importer, suivez la documentation : 
https://learning.postman.com/docs/getting-started/importing-and-exporting-data/#importing-data-into-postman

## Tests

### Tests Frontend

#### Tests E2E

Lancer les tests E2E :
```bash
cd front
npm run e2e
```

Générer le rapport de couverture (après avoir lancé les tests) :
```bash
cd front
npm run e2e:coverage
```

Le rapport est disponible ici : `front/coverage/lcov-report/index.html`

#### Tests unitaires

Lancer les tests unitaires :
```bash
cd front
npm run test
```

Mode watch pour le développement :
```bash
cd front
npm run test:watch
```

### Tests Backend

Lancer les tests et générer le rapport de couverture Jacoco :
```bash
cd back
mvn clean test
```


