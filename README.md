# JavaFX - Travelogue

JavaFX project for the PCD course at Telecom Nancy.

This project is a travelogue application that allows you to create and edit and export travelogues.
For each day of the trip, you can add a title, a description and an photo.

---

## Original README

# TP2 - PCD

TP de PCD sur l'implantation d'une application de carnet de voyage en JavaFX et FXML.

[![Sujet du projet](https://img.shields.io/badge/Sujet%20du%20projet-red)](https://github.com/teanup/JavaFX-Travelogue/blob/master/Carnet_de_voyage_2023-2024.pdf)

## Développement

### Compilation

```bash
./gradlew build
```

### Exécution

```bash
./gradlew run
```

## Archive exécutable (jar)

### Compilation

```bash
./gradlew jar
```

### Exécution

```bash
cd dist
java --module-path ${JAVAFX_HOME}/lib --add-modules=javafx.base,javafx.controls,javafx.fxml -jar carnet.jar
```

## Raccourcis clavier

* <kbd>Ctrl</kbd> + <kbd>N</kbd> : Créer un nouveau carnet
* <kbd>Ctrl</kbd> + <kbd>O</kbd> : Ouvrir un carnet (depuis un fichier JSON)
* <kbd>Ctrl</kbd> + <kbd>S</kbd> : Quitter l'application

### Lorsqu'un carnet est ouvert

* <kbd>Ctrl</kbd> + <kbd>I</kbd> : Modifier les informations du carnet (titre, auteur, dates, participants)
* <kbd>Ctrl</kbd> + <kbd>E</kbd> : Exporter le carnet au format JSON
* <kbd>Ctrl</kbd> + <kbd>G</kbd> : Mode de vue globale
* <kbd>Ctrl</kbd> + <kbd>P</kbd> : Mode de vue par page

### En mode de vue page

* <kbd>Ctrl</kbd> + <kbd>←</kbd> : Page précédente
* <kbd>Ctrl</kbd> + <kbd>→</kbd> : Page suivante
* <kbd>Ctrl</kbd> + <kbd>M</kbd> : Modifier la page courante (titre, description, image)
