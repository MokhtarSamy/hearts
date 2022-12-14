# My Sweet Heart 
## Application sur smartwatch avec son dashboard associé

## Membres du groupes 

Eva Radu, Mokhtar Samy, Mia Swery

## Etapes à réaliser pour lancer le projet : 

### Lancement de l'application : 

#### Procédure de connection entre un ordinateur et une montre : 
* Connecter la montre sur le même wifi que celui utilisé par l'ordinateur
* Dans les paramètres de la montre, activer le mode développeur sur la montre  
* Dans les paramètres de mode développeur, activer le mode débuggage avec le wifi
* Depuis le terminal de l'ordinateur, se rendre dans son dossier ``` AppData\Local\Android\Sdk\platform-tools ```
* Récupérer l'adresse ip disponible dans la section du mode débuggage avec le wifi sur la montre 
* Lancer la commande suivante dans le terminal de l'ordinateur :    ``` .\adb.exe connect [ip de la montre] ```

#### L'apk est disponible dans le dossier ```apk```
* Pour installer l'apk sur une montre, effectuer les commandes suivantes après avoir connecté sa montre à l'ordinateur : 
   Copier le fichier ``` app-debug.apk ``` dans son dossier ``` AppData\Local\Android\Sdk\platform-tools ```
   ```  .\adb.exe push app-debug.apk /sdcard/  ```
   ```  .\adb.exe -e install app-debug.apk ``` 
   
* Pour installer l'apk sur l'emulateur, on doit désintaller l'application avec la commande adb suivante:
 ```.\adb.exe uninstall "com.example.smartwatchapp"  ``` et après on glisse l'apk dans l'emulateur pour l'installer.

#### Sans utiliser l'apk il est possible de déployer l'application depuis un ordinateur de la manière suivante : 
* Ouvrir le projet ``` hearts/application/app/ ``` dans Android Studio 
* Lancer l'application

### Lancement du dashboard : 

* Depuis un terminal, se rendre dans le dossier ``` hearts/application/dashboard/ ```
* Lancer la commande suivante : ``` ng serve ```
* Se rendre sur l'url donnée par la commande précédente

### Notes sur l'utillisation de l'application : 

* Les battements cardiaques étant collectés toutes les minutes, l'affichage du battement cardiaque n'est donc pas immédiat dès que l'application est lancée
* Les moyennes, maximum et minimum sont affichés par rapport au jour actuel, leurs données seront alors vides si l'application est ouverte pour la première fois de la journée et qu'aucun battement de coeur n'a encore été collecté. 
