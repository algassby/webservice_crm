# webservice_crm
Ynov projet master webservice
## INSTALLATION DU PROJET
* Pour utiliser cette api, il faudra suivre ces étapes à la lettre.
* Installation de java 11: https://www.oracle.com/java/technologies/javase/jdk11-archive-downloads.html
* Insallation de maven: https://maven.apache.org/install.html et https://mkyong.com/maven/how-to-install-maven-in-windows/
* Installer xampp ou wamp pour utiliser Mysql
* Demarrer apacher et mysql
* Tapez la commande suivante: git clone origin main git@github.com:algassby/webservice_crm.git: recuperer la branche main pour tester le projet.
* Aller dans la racine du projet, ensuite saisir les commandes maven suivantes:
  + mvn clean install, installation des dependances et build du projet
  + mvn spring-boot:run, lancement du projet
## INSTALLATION DU PROJET AVEC DOCKER
* <p>Si vous possedez Docker il suffit de cloner la branche main</p>
* <p>Ensuite, se rendre dans la racine du projet et saisir la commande docker-compose up --build</p>
<p>Après utiliser le port 6969 pour lancer le projet en local</p>
# Après ces deux commandes, l'api vas demarrer en creant votre base de données s'il elle n'existe pas, ensuite elle va créer les tables en remplissant les tables roles et utilisateurs.
# Un utilisation ayant les informations:
* username:admin
* userKey:123456789
<p>Il va vous permettre de creer les autres admin, des organisations, des clients, des customers et rajouter des photos pour le customers.
les images des customers crées sont enregistrées dans dans dossier uploads/customer/customerId.
Chaque customers aura son propre repertoire (identificant) pour stocker ses photos.
La même logique s'applique aux oranisation (uploads/organization/customerId).</p><br/>
<p>Quand un admin ajoute ou supprime un autre admin, une organisation ou un customer, l'action est sauvegarder dans la table Logs.</p>
<p>Ensuite on peut voir les logs, les filtrer aussi par le username d'un admin.</p><br/>
<p>Une nouvelle fonctionnalité d'alerte est prevue pour sauvergarder des alertes quand un admin ajoute plusieurs organisations ou clients ou les supprime.<p> 
  
 # NB:
 Attention il faut avoir un compte gmail valid pour recevoir le mail contenant la clé de l'api pour la connexion et le lien de la documentation.<br/>
 Nous restons à votre dispostion pour toutes demandes concernant le fonctionnement de l'api. <br/>
 L'envoie du mail se fait de manière asynchrone, ce qui fait qu'on redonne la main rapidement à l'utilisateur.<br/>
 C'est à dire on ajoute l'utilisateur,  ensuite on envoie le mail independant de l'ajout.


