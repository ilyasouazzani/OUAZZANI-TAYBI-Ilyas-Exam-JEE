# Gestion des Contrats d'Assurance

Projet d'examen JEE - Architecture Distribuee  
ENSET Mohammedia - Filiere BDCC  
Etudiant : OUAZZANI TAYBI Ilyas

---

## Technologies utilisees

**Backend**
- Java 17 / Spring Boot 3.2.5
- Spring Data JPA / Hibernate
- Spring Security + JWT (JJWT 0.12.3)
- Base de donnees H2 (dev) / MySQL (prod)
- Swagger / SpringDoc OpenAPI 2.5
- Lombok / Maven

**Frontend**
- Angular 17 (Standalone Components)
- TypeScript
- RxJS / HttpClient
- JWT Interceptor

---

## Structure du projet

```
OUAZZANI-TAYBI-Ilyas-Exam-JEE/
├── gestion-contrats-backend/       # Spring Boot
│   └── src/main/java/ma/enset/gestioncontrats/
│       ├── entities/               # Client, Contrat (JOINED), AppUser
│       ├── repositories/           # Spring Data JPA
│       ├── services/               # Logique metier
│       ├── dtos/                   # Request / Response DTOs
│       ├── mappers/                # Mappers manuels
│       ├── controllers/            # REST Controllers + Swagger
│       └── security/               # JWT, Filter, SecurityConfig
└── gestion-contrats-frontend/      # Angular 17
    └── src/app/
        ├── models/                 # Interfaces TypeScript
        ├── services/               # HttpClient services
        ├── components/             # Login, Clients, Contrats, Navbar
        ├── guards/                 # AuthGuard
        └── interceptors/           # JWT Interceptor
```

---

## Lancer le backend

```bash
cd gestion-contrats-backend
mvn spring-boot:run
```

Le serveur demarre sur : http://localhost:8085

Interface H2 (base en memoire) : http://localhost:8085/h2-console  
- JDBC URL : `jdbc:h2:mem:assurancedb`  
- Username : `sa`  
- Password : (vide)

Documentation Swagger : http://localhost:8085/swagger-ui.html

---

## Lancer le frontend

```bash
cd gestion-contrats-frontend
npm install
ng serve
```

L'application Angular demarre sur : http://localhost:4200

---

## Comptes de test

| Utilisateur | Mot de passe | Role        | Acces                              |
|-------------|--------------|-------------|-------------------------------------|
| admin       | admin123     | ROLE_ADMIN  | Acces total (CRUD complet)         |
| employe     | employe123   | ROLE_EMPLOYE| Lecture clients, gestion contrats  |
| client      | client123    | ROLE_CLIENT | Lecture contrats uniquement        |

---

## Endpoints principaux

### Authentification (public)
| Methode | URL                  | Description              |
|---------|----------------------|--------------------------|
| POST    | /api/auth/login      | Connexion, retourne JWT  |
| POST    | /api/auth/register   | Inscription              |

### Clients (EMPLOYE, ADMIN)
| Methode | URL                         | Description              |
|---------|-----------------------------|--------------------------|
| GET     | /api/clients                | Liste paginee            |
| GET     | /api/clients/search?keyword | Recherche                |
| GET     | /api/clients/{id}           | Detail                   |
| POST    | /api/clients                | Creer (ADMIN)            |
| PUT     | /api/clients/{id}           | Modifier (ADMIN)         |
| DELETE  | /api/clients/{id}           | Supprimer (ADMIN)        |

### Contrats
| Methode | URL                          | Description              |
|---------|------------------------------|--------------------------|
| GET     | /api/contrats                | Liste paginee            |
| GET     | /api/contrats/client/{id}    | Contrats d'un client     |
| GET     | /api/contrats/statut/{s}     | Filtrer par statut       |
| POST    | /api/contrats/auto           | Creer contrat auto       |
| POST    | /api/contrats/habitation     | Creer contrat habitation |
| POST    | /api/contrats/sante          | Creer contrat sante      |
| PUT     | /api/contrats/{id}/valider   | Valider un contrat       |
| PUT     | /api/contrats/{id}/resilier  | Resilier un contrat      |

### Paiements
| Methode | URL                              | Description            |
|---------|----------------------------------|------------------------|
| POST    | /api/paiements                   | Enregistrer paiement   |
| GET     | /api/paiements/contrat/{id}      | Paiements d'un contrat |
| GET     | /api/paiements/contrat/{id}/total| Total paye             |

---

## Strategie d'heritage JPA

Les contrats utilisent la strategie **JOINED** :
- Table `CONTRAT` : champs communs (id, montant, statut, taux, duree...)
- Table `CONTRAT_AUTO` : immatriculation, marque, modele
- Table `CONTRAT_HABITATION` : type, adresse, superficie
- Table `CONTRAT_SANTE` : niveauCouverture, nbPersonnes

Chaque table enfant partage la cle primaire de la table parente.

---

## Securite JWT

1. Le client envoie `POST /api/auth/login` avec ses credentials
2. Le backend verifie via `UserDetailsService` + `BCrypt`
3. Un token JWT signe (HS256, validite 24h) est retourne
4. Le frontend stocke le token dans `localStorage`
5. L'intercepteur Angular ajoute `Authorization: Bearer <token>` sur chaque requete
6. `JwtAuthenticationFilter` valide le token et injecte l'utilisateur dans le `SecurityContext`
