# SYSTEME DE CLAVARDAGE DISTRIBUE MULTI-UTILISATEUR TEMPS REEL
_L’activité en voie de devenir la plus populaire est le chat (clavardage) entre amis bien que la télévision occupe toujours la part du lion de leurs activités de loisir._ - **Monique Lebrun**  
Le clavardage est un terme québécois forgé sur les mots _clavier_ et _bavardage_.  
Développé en java dans le cadre du cours de **Conception et Programmation Orientée Objet** à l'**INSA de Toulouse**.  
Ce système au nom savant permet à plusieurs utilisateurs situés sur un même réseau de communiquer via des messages textes.  
En utilisant le **PresenceManagementServer**, il est possible de communiquer avec des utilisateurs du système qui ne sont pas situés sur le même réseau.  

## Dépendances
Le système a été pré-compilé avec [openjdk11](https://openjdk.java.net/projects/jdk/11/) donc si la version de java sur votre machine n'est pas la même, il vous faudra peut-être recompiler le système avec le script `setup.sh` (plus d'informations plus bas).  
En cas de déploiement sur des postes **macOS** ou **Windows**. Des instructions de déploiement plus spécifiques sont précisées plus bas si la version précompilée ne fonctionne pas.

## Instructions d'installation
Les fichiers jar précompilés peuvent être trouvés dans `target/Clavardage/clavardage.jar` et dans `target/PresenceManagementServer/presencemanagementserver.jar`  
Pour les recompiler à la main, le script `setup.sh` peut être utilisé, il suffit de l'exécuter sans aucun argument.  
Si vous voulez déployer le système sur des postes Windows ou macOS et que la version précompilée ne fonctionne pas, voici les commandes à exécuter :
Créez les dossiers `target/Clavardage/` et `target/PresenceManagementServer/`
```shell
javac -d target/Clavardage/ Clavardage/src/*.java
javac -d target/PresenceManagementServer/ PresenceManagementServer/src/*.java
```
pour compiler les fichiers sources en fichiers class.  
Placez vous ensuite successivement dans les dossiers `target/Clavardage/` puis dans `target/PresenceManagementServer/` et exécutez 
```shell
jar cfe <nom_du_fichier_jar_désiré> Main *.class
```
Les fichiers .class sont inutiles à partir d'ici donc vous pouvez les supprimer.

Pour démarrer le client de chat : se placer dans `target/Clavardage/` et exécuter la commande `java -jar clavardage.jar`.  
La procédure est identique pour le PresenceManagementServer : se placer dans `target/PresenceManagementServer` et exécuter `java -jar presencemanagementserver.jar`  

## Utilisation du système
* Pour utiliser le client de clavardage, démarrer le client comme indiqué ci-dessus. Le système demande de choisir un pseudo qu'il refusera poliment si celui-ci est déjà utilisé par un autre utilisateur du système.
* Une fois sur l'interface de chat, le système affiche sur la gauche la liste des autres utilisateurs qu'il a détectés. Pour discuter avec l'un d'entre eux, sélectionner son nom avec la souris et taper le message à envoyer dans la zone de texte située en bas de l'interface. Une fois le message tapé, appuyez sur le bouton `Send` pour envoyer le message. Les messages envoyés et reçus sont affichés et horodatés sur la zone de texte centrale.
* Dans le cas d'un utilisateur distant (qui ne se situe pas sur le même réseau que ses amis), il peut néamoins clavarder avec eux grâce au **PresenceManagementServer** si celui-ci tourne sur l'une des machines de la zone démilitarisée du réseau principal (comprendre DMZ, machine directement connectée au réseau et à l'internet). L'utilisateur peut utiliser le bouton `Connect to server` et rentrer l'adresse IP de cette machine. Il apparaîtra dans la liste des utilisateurs connectés sur les autres machines et verra apparaître les autres utilisateurs connectés et pourra démarrer une session de clavardage avec l'un d'entre eux.
* Le PresenceManagementServer ne nécessite pas d'intervention de l'administrateur. Une fois démarré il est automatiquement détecté par les utilisateurs situés sur le réseau et détecte également ces utilisateurs.

## Bogues connus (bugs pour les anglophones)
* Quand un utilisateur change son pseudonyme, il faut cliquer sur la liste des utilisateurs pour que le pseudonyme se mette à jour dans l'interface graphique.
* Pour enregistrer l'historique de clavardage, il faut cliquer sur le bouton `logout`. Fermer l'application sans se déconnecter causera la perte des derniers messages envoyés. 

## Dégagement de responsabilité (Disclaimer pour les anglophones)
Ce Bureau d'Etudes était censé se dérouler en présentiel. En temps normal nous disposons de matériel spécifique pour pouvoir tester l'application ce qui n'a pas été possible cette année pour causes sanitaires. En conséquence, la plupart des tests ont été effectués sur une seule machine, ce qui a pu conduire a l'éventuelle non détection de certains bogues. Nous prions l'utilisateur final de ce système s'il existe (l'utilisateur, pas le système) de reporter les éventuels problèmes trouvés sur github.
