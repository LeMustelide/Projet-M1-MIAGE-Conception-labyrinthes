# Projet Labyrinthe Java

Ce projet permet de créer un labyrinthe parfait et de le visualiser sous forme de SVG.

## Auteurs
- Marc Virgili
- Enzo Labbe

## Principe de l'algorithme utilisé

L'algorithme de construction du labyrinthe employé dans ce projet est basé sur la **fusion aléatoire de chemins**.  qui est optimisée par l'utilisation d'une structure de données appelée **Union-Find**.

### Fusion aléatoire de chemins

La fusion aléatoire de chemins se base sur une propriété des labyrinthes parfaits : chaque cellule est reliée à toutes les autres de manière unique. L'algorithme fonctionne en fusionnant progressivement des chemins depuis la simple cellule jusqu'à l'obtention d'un chemin unique.

Le processus est le suivant :

1. Une valeur unique est associée à chaque cellule (leur numéro de séquence, par exemple). Au départ, le labyrinthe est considéré comme ayant tous les murs fermés.
2. À chaque itération, un mur est choisi aléatoirement pour être ouvert.
3. L'ouverture d'un mur entre deux cellules adjacentes les relie et forme un chemin.
4. Avant d'ouvrir un mur entre deux cellules, l'algorithme vérifie que ces cellules ont des identifiants différents :
    - Si les identifiants sont identiques, les cellules sont déjà reliées et appartiennent au même chemin. Le mur reste donc fermé.
    - Si les identifiants sont différents, le mur est ouvert, et l'identifiant de la première cellule est attribué à toutes les cellules du second chemin.
5. Le processus se termine lorsqu'un chemin unique est obtenu, ce qui est le cas quand le nombre de murs ouverts atteint \(mn-1\).

L'apport principal de notre travail réside dans l'adoption d'une structure Union-Find optimisée, intégrant l'union par rang et la compression de chemin. Cette optimisation permet une réduction notable de la complexité des opérations de vérification d'appartenance et de fusion, garantissant ainsi une génération du labyrinthe nettement plus efficiente, particulièrement pour des dimensions élevées.

## Structure de données

- `Labyrinth`: La classe principale qui représente un labyrinthe. Elle contient des murs verticaux et horizontaux, et utilise l'Union-Find pour générer un labyrinthe parfait.
    - `verticalWalls`: Un tableau de booléens représentant les murs verticaux.
    - `horizontalWalls`: Un tableau de booléens représentant les murs horizontaux.
    - `uf`: Une instance de la classe UnionFind utilisée pour la génération du labyrinthe.
    - `start` et `end`: Les points de départ et d'arrivée du labyrinthe.

- `LabyrinthSVGGenerator`: Cette classe génère une représentation SVG du labyrinthe pour une visualisation graphique.

- `UnionFind`: Une classe représentant une structure de données Union-Find. Elle est utilisée pour vérifier l'appartenance d'une cellule à un groupe et fusionner deux groupes.
    - `group`: Un tableau représentant les groupes des cellules.

- `Main`: La classe principale qui génère le labyrinthe et enregistre sa représentation SVG dans un fichier.

## Compilation

Pour compiler l'application, utilisez la commande suivante :

```bash
mvn package
```


## Execution

Pour exécuter l'application, après avoir compilé, utilisez la commande suivante :

```bash
java -jar Projet-M1-MIAGE-Conception-labyrinthes-1.0.1.jar
```


Cela générera un labyrinthe parfait, le convertira en SVG, et sauvegardera cette représentation SVG dans un fichier appelé `labyrinthe.svg`.

## Note

La sortie du labyrinthe est visualisée en vert, tandis que l'entrée est en rouge.

--- 

