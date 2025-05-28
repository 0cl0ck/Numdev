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

#### Tests Unitaires et d'Intégration (Jest)

Lancer les tests unitaires :
```bash
cd front
npm run test
```

Générer le rapport de couverture :
```bash
cd front
npm run test:coverage
```

Le rapport est disponible ici : `front/coverage/lcov-report/index.html`

Mode watch pour le développement :
```bash
cd front
npm run test:watch
```

#### Tests E2E (Cypress)

##### Méthode standard :

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

##### Méthode avec instrumentation complète (pour la couverture) :

1. Démarrer le serveur en mode instrumenté (nécessaire pour la couverture) :
```bash
cd front
ng run yoga:serve-coverage
```

2. Dans un autre terminal, lancer les tests avec couverture :
```bash
cd front
npm run e2e:run
```

3. Générer le rapport de couverture :
```bash
cd front
npm run e2e:coverage
```

### Tests Backend (Spring Boot)

Lancer les tests backend :
```bash
cd back
mvn test
```

Générer le rapport de couverture JaCoCo :
```bash
cd back
mvn verify
```

Le rapport est disponible ici : `back/target/site/jacoco/index.html`

## Résultats des Tests

Les taux de couverture actuels sont :

- **Frontend (Jest)** : 
  - Statements: 99.54%
  - Branches: 94.11%
  - Functions: 98.38%
  - Lines: 100%

- **Backend (Spring Boot/JaCoCo)** : 
  - 84% de couverture globale

- **Tests E2E (Cypress)** : 
  - Statements: 93.65%
  - Branches: 95.45%
  - Functions: 92.7%
  - Lines: 92.69%

Total de tests : 210+ tests répartis entre le frontend, backend et E2E.
