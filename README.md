# webservice_crm
Ynov projet master webservice
* Pour utiliser cette api, il faudra suivre ces étapes à la lettre.
* Installation de java 11: https://www.oracle.com/java/technologies/javase/jdk11-archive-downloads.html
* Insallation de maven: https://maven.apache.org/install.html et https://mkyong.com/maven/how-to-install-maven-in-windows/
* Installer xampp ou wamp pour utiliser Mysql
* Demarrer apacher et mysql
* Tapez la commande suivante: git clone origin master git@github.com:algassby/webservice_crm.git
* Aller dans la racine du projet, ensuite saisir les commandes maven suivantes:
  + mvn clean install, installation des dependances et build du projet
  + mvn spring-boot:run, lancement du projet

# Après ces deux commandes, l'api vas demarrer en creant votre base de données s'il elle n'existe et les tables en remplissant la table Role et utilisateurs.
# Un utilisation ayant les informations:
* username:admin
* userKey:123456789
<p>Il va vous permettre de creer les autres admin, des organisations, des clients, des customers et rajouter des photos pour le customers.
les images des customers crées sont enregistrées dans dans dossier uploads/customerId.
Chaque customers aura son propre repertoire (identificant) pour stocker ses photos.</p>


