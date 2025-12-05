# 🛒 SmartShop - Application de Gestion de Boutique

[![Java](https://img.shields.io/badge/Java-17-orange.svg)](https://www.oracle.com/java/)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.5.7-brightgreen.svg)](https://spring.io/projects/spring-boot)
[![PostgreSQL](https://img.shields.io/badge/PostgreSQL-Latest-blue.svg)](https://www.postgresql.org/)
[![Maven](https://img.shields.io/badge/Maven-3.8+-red.svg)](https://maven.apache.org/)
[![License](https://img.shields.io/badge/License-MIT-yellow.svg)](LICENSE)

## 📋 Table des matières

- [À propos](#à-propos)
- [Fonctionnalités](#fonctionnalités)
- [Technologies utilisées](#technologies-utilisées)
- [Architecture](#architecture)
- [Prérequis](#prérequis)
- [Installation](#installation)
- [Configuration](#configuration)
- [Lancement](#lancement)
- [Documentation API](#documentation-api)
- [Tests](#tests)
- [Exemples d'utilisation](#exemples-dutilisation)
- [Auteur](#auteur)

---

## 📖 À propos

**SmartShop** est une application REST API complète de gestion de boutique en ligne développée avec **Spring Boot**. Elle permet de gérer l'ensemble du cycle commercial : clients, produits, commandes et paiements, avec un système de fidélité client et de codes promotionnels.

### Points forts

✅ Système de fidélité à 4 niveaux (BASIC, SILVER, GOLD, PLATINIUM)  
✅ Gestion des paiements multiples (espèce, chèque, virement)  
✅ Calcul automatique des remises et de la TVA  
✅ Contrôle de stock en temps réel  
✅ Authentification sécurisée avec BCrypt  
✅ Documentation API interactive avec Swagger  
✅ Tests unitaires avec couverture de code  

---

## 🚀 Fonctionnalités

### 👥 Gestion des Clients
- Création et gestion des comptes clients
- Système de fidélité progressif basé sur l'historique d'achats
- Attribution automatique de remises selon le niveau (5%, 10%, 15%)
- Suivi du montant total dépensé et nombre de commandes

### 📦 Gestion des Produits
- CRUD complet (Create, Read, Update, Delete)
- Gestion du stock avec vérifications automatiques
- Soft delete (suppression logique)

### 🛒 Gestion des Commandes
- Création de commandes avec plusieurs produits
- Application de codes promotionnels
- Calcul automatique :
  - Sous-total
  - Remises (fidélité + promotionnelle)
  - Montant HT
  - TVA (20%)
  - Total TTC
- Suivi du statut (EN_ATTENTE, CONFIRME, ANNULE, REJETE)
- Validation avant confirmation (paiement complet + stock disponible)

### 💳 Gestion des Paiements
- Paiements multiples pour une même commande
- **Espèces** : Encaissement immédiat (limite 20 000 DH)
- **Chèque** : Validation manuelle avec suivi d'échéance
- **Virement** : Validation manuelle avec référence bancaire
- Suivi du montant restant à payer

### 🔐 Sécurité
- Authentification par sessions HTTP
- Hashage des mots de passe avec BCrypt
- Contrôle d'accès par rôles (ADMIN/CLIENT)
- Intercepteur de sécurité sur toutes les routes

---

## 🛠️ Technologies utilisées

### Backend
- **Java 17** - Langage de programmation
- **Spring Boot 3.5.7** - Framework principal
  - Spring Web - API REST
  - Spring Data JPA - Persistance des données
  - Spring Validation - Validation des données
- **PostgreSQL** - Base de données relationnelle
- **Hibernate** - ORM

### Outils et bibliothèques
- **Lombok** - Réduction du code boilerplate
- **MapStruct 1.5.5** - Mapping Entity ↔ DTO
- **BCrypt (jBCrypt)** - Hashage des mots de passe
- **SpringDoc OpenAPI** - Documentation Swagger
- **JUnit 5** - Tests unitaires
- **Mockito** - Mocking pour les tests
- **JaCoCo** - Couverture de code
- **Maven** - Gestion des dépendances

---

## 🏗️ Architecture

L'application suit une **architecture en couches** pour une séparation claire des responsabilités :

```
┌─────────────────────────────────────────┐
│          CLIENT (Postman/Web)           │
└────────────────┬────────────────────────┘
                 │ HTTP (JSON)
┌────────────────▼────────────────────────┐
│       🔒 AuthInterceptor                 │
│    (Vérification session & droits)       │
└────────────────┬────────────────────────┘
                 │
┌────────────────▼────────────────────────┐
│       📡 CONTROLLER LAYER                │
│   (Endpoints REST + Validation)          │
└────────────────┬────────────────────────┘
                 │
┌────────────────▼────────────────────────┐
│       ⚙️ SERVICE LAYER                   │
│   (Logique métier + Validations)         │
└────────────────┬────────────────────────┘
                 │
┌────────────────▼────────────────────────┐
│       💾 REPOSITORY LAYER                │
│   (Accès données via JPA)                │
└────────────────┬────────────────────────┘
                 │
┌────────────────▼────────────────────────┐
│       🗄️ DATABASE (PostgreSQL)           │
└─────────────────────────────────────────┘
```

### Modèle de données

```
USER (1:1) CLIENT (1:N) ORDER (1:N) ORDER_ITEM (N:1) PRODUIT
                          ↓
                      PAIEMENT (N:1)
                          ↓
                      PROMO_CODE (N:1)
```

---

## 📋 Prérequis

Avant de commencer, assurez-vous d'avoir installé :

- **Java 17** ou supérieur - [Télécharger](https://www.oracle.com/java/technologies/downloads/)
- **Maven 3.8+** - [Télécharger](https://maven.apache.org/download.cgi)
- **PostgreSQL 12+** - [Télécharger](https://www.postgresql.org/download/)
- **Git** - [Télécharger](https://git-scm.com/downloads)
- **Postman** (optionnel) - [Télécharger](https://www.postman.com/downloads/)

---

## 📥 Installation

### 1. Cloner le dépôt

```bash
git clone https://github.com/votre-username/smartShop.git
cd smartShop
```

### 2. Créer la base de données

Connectez-vous à PostgreSQL et exécutez :

```sql
CREATE DATABASE smartShop;
```

### 3. Configurer l'application

Modifiez le fichier `src/main/resources/application.yaml` selon votre configuration PostgreSQL :

```yaml
spring:
  datasource:
    url: jdbc:postgresql://localhost:5432/smartShop
    username: postgres      # Votre username PostgreSQL
    password: root          # Votre mot de passe PostgreSQL
```

### 4. Installer les dépendances

```bash
mvn clean install
```

---

## ⚙️ Configuration

### Variables d'environnement (optionnel)

Vous pouvez également utiliser des variables d'environnement :

```bash
export DB_URL=jdbc:postgresql://localhost:5432/smartShop
export DB_USERNAME=postgres
export DB_PASSWORD=root
```

Et modifier `application.yaml` :

```yaml
spring:
  datasource:
    url: ${DB_URL}
    username: ${DB_USERNAME}
    password: ${DB_PASSWORD}
```

### Configuration du port (par défaut : 8081)

Pour changer le port, modifiez dans `application.yaml` :

```yaml
server:
  port: 8081  # Changez selon vos besoins
```

---

## 🚀 Lancement

### Avec Maven

```bash
mvn spring-boot:run
```

### Avec Java

```bash
mvn clean package
java -jar target/smartShop-0.0.1-SNAPSHOT.jar
```

### Vérification

L'application démarre sur : **http://localhost:8081**

Testez avec :
```bash
curl http://localhost:8081/api/auth/session
```

---

## 📚 Documentation API

### Swagger UI

Une fois l'application lancée, accédez à la documentation interactive :

👉 **http://localhost:8081/swagger-ui.html**

Vous pourrez :
- Voir tous les endpoints disponibles
- Tester les requêtes directement
- Consulter les schémas de données

### Endpoints principaux

| Méthode | Endpoint | Description | Accès |
|---------|----------|-------------|-------|
| **POST** | `/api/auth/login` | Connexion | Public |
| **POST** | `/api/auth/logout` | Déconnexion | Authentifié |
| **GET** | `/api/auth/session` | Session actuelle | Authentifié |
| **POST** | `/api/admin/clients` | Créer un client | ADMIN |
| **GET** | `/api/admin/clients` | Liste des clients | ADMIN |
| **GET** | `/api/admin/clients/{id}` | Détails client | ADMIN |
| **PUT** | `/api/admin/clients/{id}` | Modifier client | ADMIN |
| **DELETE** | `/api/admin/clients/{id}` | Supprimer client | ADMIN |
| **POST** | `/api/admin/produits` | Créer un produit | ADMIN |
| **GET** | `/api/client/produits` | Liste des produits | Tous |
| **PUT** | `/api/admin/produits/{id}` | Modifier produit | ADMIN |
| **DELETE** | `/api/admin/produits/{id}` | Supprimer produit | ADMIN |
| **POST** | `/api/orders` | Créer une commande | Authentifié |
| **GET** | `/api/admin/orders` | Liste des commandes | ADMIN |
| **POST** | `/api/admin/orders/{id}/confirm` | Confirmer commande | ADMIN |
| **POST** | `/api/admin/paiements` | Créer un paiement | ADMIN |
| **PATCH** | `/api/admin/paiements/{id}/validate` | Valider paiement | ADMIN |

---

## 🧪 Tests

### Lancer tous les tests

```bash
mvn test
```

### Générer le rapport de couverture (JaCoCo)

```bash
mvn clean test jacoco:report
```

Le rapport est généré dans : `target/site/jacoco/index.html`

### Structure des tests

```
src/test/java/
└── com.houssam.smartShop/
    ├── SmartShopApplicationTests.java
    └── service/
        ├── AuthServiceTest.java
        ├── ClientServiceTest.java
        ├── OrderServiceTest.java
        ├── PaiementServiceTest.java
        └── ProduitServiceTest.java
```

---

## 💡 Exemples d'utilisation

### 1. Connexion

```bash
curl -X POST http://localhost:8081/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "username": "admin",
    "password": "admin123"
  }'
```

### 2. Créer un client

```bash
curl -X POST http://localhost:8081/api/admin/clients \
  -H "Content-Type: application/json" \
  -H "Cookie: JSESSIONID=xxx" \
  -d '{
    "nom": "Alami",
    "prenom": "Sara",
    "email": "sara@example.com",
    "user": {
      "username": "sara",
      "password": "password123"
    }
  }'
```

### 3. Créer un produit

```bash
curl -X POST http://localhost:8081/api/admin/produits \
  -H "Content-Type: application/json" \
  -H "Cookie: JSESSIONID=xxx" \
  -d '{
    "nom": "iPhone 15",
    "prixUnite": 12000,
    "stock": 10
  }'
```

### 4. Créer une commande

```bash
curl -X POST http://localhost:8081/api/orders \
  -H "Content-Type: application/json" \
  -H "Cookie: JSESSIONID=xxx" \
  -d '{
    "clientId": "client-id-uuid",
    "promoCode": "PROMO10",
    "items": [
      {
        "produitId": "produit-id-uuid",
        "quantite": 2
      }
    ]
  }'
```

### 5. Créer un paiement

```bash
curl -X POST http://localhost:8081/api/admin/paiements \
  -H "Content-Type: application/json" \
  -H "Cookie: JSESSIONID=xxx" \
  -d '{
    "orderId": "order-id-uuid",
    "montant": 15000,
    "typePaiement": "ESPECE"
  }'
```

---

## 📊 Système de Fidélité

### Niveaux et conditions

| Niveau | Conditions | Remise | Seuil minimum |
|--------|-----------|--------|---------------|
| **BASIC** | Par défaut | 0% | - |
| **SILVER** | ≥3 commandes OU ≥1000 DH | 5% | ≥500 DH |
| **GOLD** | ≥10 commandes OU ≥5000 DH | 10% | ≥800 DH |
| **PLATINIUM** | ≥20 commandes OU ≥15000 DH | 15% | ≥1200 DH |

### Calcul d'une commande

```
Sous-total = Σ (quantité × prix unitaire)
Remise promo = Sous-total × (% promo / 100)
Remise fidélité = (Sous-total - Remise promo) × (% fidélité / 100) [si seuil atteint]
Montant HT = Sous-total - Remise promo - Remise fidélité
TVA = Montant HT × 0.20
Total TTC = Montant HT + TVA
```

---

## 🔐 Sécurité

### Authentification

L'application utilise des **sessions HTTP** pour gérer l'authentification :

1. Login → Création d'une session avec cookie `JSESSIONID`
2. Chaque requête → Vérification de la session par `AuthInterceptor`
3. Logout → Invalidation de la session

### Hashage des mots de passe

Les mots de passe sont hashés avec **BCrypt** :
- Salt unique par mot de passe
- Algorithme résistant aux attaques brute force
- Impossible de retrouver le mot de passe d'origine

### Contrôle d'accès

- **ADMIN** : Accès complet à toutes les routes
- **CLIENT** : Accès limité (pas d'accès aux routes `/api/admin/*`)

---

## 🚧 Limitations et règles métier

- ❗ Paiement en espèces limité à **20 000 DH** (contrainte légale marocaine)
- ❗ Une commande ne peut être confirmée que si **montantRestant = 0**
- ❗ Stock vérifié avant confirmation de commande
- ❗ Les paiements par chèque/virement nécessitent une **validation manuelle**
- ❗ Les produits sont **soft deleted** (pas de suppression physique)

---

## 🐛 Résolution de problèmes

### Erreur de connexion à la base de données

```
Caused by: org.postgresql.util.PSQLException: Connection refused
```

**Solution** : Vérifiez que PostgreSQL est démarré et que les identifiants dans `application.yaml` sont corrects.

### Port 8081 déjà utilisé

```
Web server failed to start. Port 8081 was already in use.
```

**Solution** : Changez le port dans `application.yaml` ou arrêtez l'application qui utilise le port 8081.

### Tests échouent

```
mvn test
```

**Solution** : Assurez-vous que la base de données est accessible et que les dépendances sont installées (`mvn clean install`).

---

## 📝 Améliorations futures

- [ ] Authentification JWT pour scalabilité
- [ ] Système de notifications par email
- [ ] Filtres et recherche avancée sur les produits
- [ ] Dashboard d'administration
- [ ] Export des rapports (PDF, Excel)
- [ ] Conteneurisation avec Docker
- [ ] CI/CD avec GitHub Actions
- [ ] Monitoring avec Spring Actuator

---

## 👤 Auteur

**Houssam**

- GitHub : [@lambarahoussam](https://github.com/votre-username)
- Email : lambarahoussam@gmail.com

---

**Made by Houssam**

