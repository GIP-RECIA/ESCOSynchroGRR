# ESCOSynchroGRR
Outil de synchronisation de GRR depuis l'annuaire LDAP

Présentation
============

Ce document présente la procédure d’installation et de fonctionnement du batch
de synchronisation du LDAP de l’ENT vers l’application GRR.

Généralités
===========

Prérequis
----------

Testé et qualifié avec :
- Java 11
- Maven 3.6.3

Compilation du batch avec Maven
===============================

Ouvrir une console à la racine du projet (répertoire Grr-Sync), là où se trouve le fichier pom.xml

Lancer la commande :
```bash
mvn package
```
pour lancer la compilation du projet et obtenir un jar de lancement (le paramètre -DskipTests=true permet de passer les tests si besoin).

Le jar se trouvera dans le répertoire target sous le nom de « metier-1.0.jar ».

Préparation des fichiers
=========================

Le batch nécessite à son fonctionnement trois fichiers de configuration, qui  devront être regroupés dans 
un dossier commun (ex: config)( le chemin de ce dossier sera un paramètre du batch ) :


config.properties
-----------------

-   L’ensemble des paramètres nécessaires au fonctionnement du batch sont stockés dans un fichier de propriétés (config.properties)
    externe afin de pouvoir être modifiés facilement et pris en compte par le batch sans recompilation de celui-ci.

| **Attribut application.properties** | **Information**                                                                                                                                                                         |
|-------------------------------------|-----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| spring.datasource.username          | Nom d’utilisateur de la base multi Etablissement. Utilisé dans la migration et la synchronisation                                                                                       |
| spring.datasource.password          | Mot de passe de la base multi Etablissement. Utilisé dans la migration et la synchronisation                                                                                            |
| spring.jpa.database                 | Type de base de données. Laisser la valeur "default"                                                                                                                                    |
| migration.datasource.url            | Url de la base source de la migration                                                                                                                                                   |
| migration.datasource.username       | Nom d’utilisateur de la base source de la migration.                                                                                                                                    |
| migration.datasource.password       | Mot de passe de la base source de la migration.                                                                                                                                         |
| migration.cible.codeetablissement   | Utilisé pour le code de l’établissement cible dans le cas d’une migration (présent dans la base de données destination).                                                                |
| spring.ldap.urls                    | URL du ldap ( ex: ldap://xxxxxx:389 )                                                                                                                                                   |
| spring.ldap.base                    | Base du LDAP ( ex: dc=esco-centre,dc=fr )                                                                                                                                               |                                                                                                                                          
| spring.ldap.base_personnes          | Base de la branche des personnes                                                                                                                                                        |                                                                                                                                           
| spring.ldap.base_etablissements     | Base de la branche des établissements                                                                                                                                                   |
| spring.ldap.username                | Nom d’utilisateur du LDAP                                                                                                                                                               |
| spring.ldap.password                | Mot de passe du LDAP                                                                                                                                                                    |
| createSiteOnCreateEtab              | Si le paramètre du batch createSiteOnCreateEtab est égal à « true », il faut créer les enregistrements « Site » et « Area »                                                             |
| derniereConnexionDepuisNJour        | Utilisé pour la suppression dans la base GRR des utilisateurs qui ne sont plus présents dans le LDAP et qui ne se sont pas connecté depuis plus de « n » jours (n= jourRequisConnexion) |
| anneeReservationTropAncienne        | Utilisé pour la suppression dans la base GRR des réservations datant de plus de «n» années (n= anneeReservationTropAncienne )                                                           |
| anneeViderLog                       | Utilisé pour la suppression dans la base GRR des enregistrements de log datant de plus de «n» années (n= anneeViderLog)                                                                 |
| nomDomainesParDefault               | Le nom des domaines par défaut est paramétré dans le fichier de propriétés du batch sous forme d’une chaine de caractères avec « \$ » comme séparateur (ex : « Salles$Materiel »)       |
| typeDeDomaineParDefault             | Type de domaine par défaut correspond à la colonne ‘access’ dans grr_area.                                                                                                              |
| emailParDefaut                      | Utilisé pour les comptes n'ayant pas de mail dans le ldap.                                                                                                                              |
| statutAdministrateur                | Pattern pour identifier un admin central                                                                                                                                                |
| statutAdminEtablissement            | Pattern pour identifier un admin local                                                                                                                                                  |
| statutUtilisateur                   | Pattern pour identifier un utilisateur                                                                                                                                                  |
| statutCodeEtablissement             | Pattern pour identifier un établissement                                                                                                                                                |
| statutSpeicalGIPRecia               | Pattern pour identifier un utilisateur du GIP Recia                                                                                                                                     |
| statutAdminGIPRecia                 | Pattern pour identifier un admin local du GIP Recia                                                                                                                                     |            
| etablissementPrincipal              | Pattern pour identifier un établissement principal                                                                                                                                      |
| etablissementPrincipal2             | Pattern pour identifier un établissement secondaire                                                                                                                                     |
| filter.ldap.structures              | Filtre LDAP pour récupérer les structures de GRR                                                                                                                                        |
| filter.ldap.people                  | Filtre LDAP pour récupérer les utilisateurs de GRR                                                                                                                                      |



dateDerniereMiseAJour.properties
--------------------------------

-   La date de dernier passage du batch est stockée dans un fichier texte qui
    sera mis à jour à chaque exécution du batch (dateDerniereMiseAJour.properties).
    Cette date permet de traiter les mises à jour en mode «delta» en ne traitant 
    que les entrées LDAP modifiées depuis le dernier passage du Batch.

| **Attribut dateDerniereMiseAJour.properties** | **Information**                                          |
|-----------------------------------------------|----------------------------------------------------------|
| date                                          | Date de la dernière mise à jour Exemple: 20180218000000Z |

regroupementEtablissements.properties
-------------------------------------

-   Les relations entre les établissements principaux et les établissements
    secondaires (regroupementEtablissements.properties) se font de la manière
    suivante : UAI_SECONDAIRE.principal=UAI_PRINCIPAL

| **Attribut regroupementEtablissements.properties**           | **Valeur**                       | **Information**                                                                                  |
|--------------------------------------------------------------|----------------------------------|--------------------------------------------------------------------------------------------------|
| **\*.principal** \*est le code de l’établissement secondaire | **Code établissement principal** | Exemple: **0333333Y.principal**=**0360548A 0333333Y est le secondaire et 0360548A le principal** |

system.log
----------

L’ensemble des actions effectuées par le batch sont tracées dans un fichier de
log (system.log) présent dans le dossier Logs.

Chaque fichier correspond à un lancement. De plus chaque ligne de ce fichier est horodatée.

Les informations présentes dans ce fichier sont:

-   Date de dernière mise à jour utilisée par le batch

-   Date et heure de début et de fin d’exécution + le nom de chaque étape du
    batch

-   Identifiants des enregistrements insérés/modifiés/supprimés par le batch

-   Informations quantitatives sur le nombre
    d’insertion/modifications/suppressions à chaque étape.

Initialisation des tables Spring Batch
======================================

spring.batch.initialize-schema=always Permets d’initialiser les tables du batch automatiquement avec l’utilisateur courant.

Si celui-ci ne possède pas les droits il faut alors lancer le script
schema-mysql-spring-batch.sql avec un utilisateur les possédants et donner les
droits de lecture/écriture a l’utilisateur utilisé dans le batch.

spring.batch.initialize-schema=never Permets de désactiver l’initialisation des tables du batch automatiquement.

Lancement du batch
==================

Pour lancer le batch il faut entrer la commande suivante :
```bash
java -cp /chemin/vers/metier-1.0.jar -Dloader.path=/chemin/vers/dossier/config/ -Dlogs_dir=/chemin/vers/dossier/logs/ org.springframework.boot.loader.PropertiesLauncher
```

| **Attribut**              | **Information**                                                  |
|---------------------------|------------------------------------------------------------------|
| \-Dlogs_dir               | Emplacement du répertoire de log                                 |
| \-Dspring.profiles.active | Permet de choisir entre le synchro et la migration               |
| \-Dloader.path            | Emplacements du répertoire contenant les fichiers de propriétés. |
| \-cp                      | Emplacement du jar de l’application                              |

Si jamais pas le script crash car pas assez de mémoire, on peut rajouter les flags `-Xmx2G -Xms2G` pour donner 2Go de mémoire (ce qui est normalement suffisant).