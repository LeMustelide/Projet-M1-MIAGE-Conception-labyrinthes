# Chargement des fichiers
donnees1 <- read.csv("resultatsV1.csv")
donnees2 <- read.csv("resultatsV2.csv")
donnees3 <- read.csv("resultatsV3.csv")

# Résumés statistiques
summary(donnees1$temps)
summary(donnees2$temps)
summary(donnees3$temps)

# Histogrammes des temps d'exécution
par(mfrow=c(1,3))  # Diviser la fenêtre graphique en une matrice 1x3
hist(donnees1$temps, main="Fichier 1", xlab="Temps d'exécution")
hist(donnees2$temps, main="Fichier 2", xlab="Temps d'exécution")
hist(donnees3$temps, main="Fichier 3", xlab="Temps d'exécution")

# Boxplot des temps d'exécution
boxplot(donnees1$temps, donnees2$temps, donnees3$temps, names=c("Fichier 1", "Fichier 2", "Fichier 3"), main="Comparaison des temps d'exécution", ylab="Temps d'exécution")

# Diagrammes de densité des temps d'exécution
plot(density(donnees1$temps), main="Comparaison des densités de temps d'exécution", xlab="Temps d'exécution", ylim=c(0, max(density(donnees1$temps)$y, density(donnees2$temps)$y, density(donnees3$temps)$y)))
lines(density(donnees2$temps), col="blue")
lines(density(donnees3$temps), col="red")
legend("topright", legend=c("Fichier 1", "Fichier 2", "Fichier 3"), col=c("black", "blue", "red"), lty=1)

# Diagramme de dispersion du temps d'exécution en fonction de la taille
par(mfrow=c(1,3))  # Réinitialisation de la division de la fenêtre graphique
plot(donnees1$taille, donnees1$temps, main="Fichier 1", xlab="Taille", ylab="Temps d'exécution")
abline(lm(donnees1$temps ~ donnees1$taille), col="red")  # Ligne de tendance
plot(donnees2$taille, donnees2$temps, main="Fichier 2", xlab="Taille", ylab="Temps d'exécution")
abline(lm(donnees2$temps ~ donnees2$taille), col="red")  # Ligne de tendance
plot(donnees3$taille, donnees3$temps, main="Fichier 3", xlab="Taille", ylab="Temps d'exécution")
abline(lm(donnees3$temps ~ donnees3$taille), col="red")  # Ligne de tendance


# Fonction pour obtenir la prédiction de temps d'exécution pour une taille spécifiée
predire_temps <- function(donnees, taille) {
  modele <- lm(temps ~ taille, data=donnees)
  prediction <- predict(modele, newdata=data.frame(taille=taille))
  return(prediction)
}

# Tailles spécifiées
tailles <- c(100, 500, 1000)

# Calcul des prédictions de temps d'exécution pour chaque taille et chaque fichier
resultats <- matrix(NA, nrow=length(tailles), ncol=3, 
                    dimnames=list(tailles, c("Fichier 1", "Fichier 2", "Fichier 3")))

for (i in 1:length(tailles)) {
  resultats[i, 1] <- predire_temps(donnees1, tailles[i])
  resultats[i, 2] <- predire_temps(donnees2, tailles[i])
  resultats[i, 3] <- predire_temps(donnees3, tailles[i])
}

# Affichage des résultats
print(resultats)

# Trouver le fichier avec la prédiction de temps d'exécution la plus basse pour chaque taille
meilleur_fichier <- apply(resultats, 1, function(x) names(x)[which.min(x)])
print(meilleur_fichier)