# Chargement des fichiers
donnees1 <- read.csv("profiler/resultatsV3.csv")

donnees1 <- subset(donnees1, temps != -1)

# Résumés statistiques
summary(donnees1$temps)

# Histogrammes des temps d'exécution
par(mfrow=c(1,2))  # Diviser la fenêtre graphique en une matrice 1x2
hist(donnees1$temps, main="Fichier 1", xlab="Temps d'exécution")

# Boxplot des temps d'exécution
# boxplot(donnees1$temps, donnees2$temps, names=c("Fichier 1", "Fichier 2"), main="Comparaison des temps d'exécution", ylab="Temps d'exécution")

par(mfrow=c(1,1))
# Diagrammes de densité des temps d'exécution
plot(density(donnees1$temps), main="Comparaison des densités de temps d'exécution", xlab="Temps d'exécution", ylim=c(0, max(density(donnees1$temps)$y, density(donnees1$temps)$y)))
# lines(density(donnees2$temps), col="blue")
# legend("topright", legend=c("Fichier 1", "Fichier 2"), col=c("black", "blue"), lty=1)

# Diagramme de dispersion du temps d'exécution en fonction de la taille
plot(donnees1$taille, donnees1$temps, col="blue", pch=20, 
     main="Temps d'exécution en fonction de la taille", 
     xlab="Taille", ylab="Temps d'exécution", xlim = range(donnees1$taille), ylim = range(donnees1$temps))

# Ajout des lignes de régression
abline(lm(donnees1$temps ~ donnees1$taille), col="blue")

# Ajout d'une légende
# legend("topleft", legend=c("Fichier 1", "Fichier 2"), col=c("blue", "red"), pch=c(20, 17), bty="n")

# Fonction pour obtenir la prédiction de temps d'exécution pour une taille spécifiée
predire_temps <- function(donnees, taille) {
  modele <- lm(temps ~ taille, data=donnees)
  prediction <- predict(modele, newdata=data.frame(taille=taille))
  return(prediction)
}

# Tailles spécifiées
tailles <- c(100, 500, 1000, 2000, 5000)

# Calcul des prédictions de temps d'exécution pour chaque taille et chaque fichier
resultats <- matrix(NA, nrow=length(tailles), ncol=1, 
                    dimnames=list(tailles, "Labyrinthe"))

for (i in 1:length(tailles)) {
  resultats[i, 1] <- predire_temps(donnees1, tailles[i])
}

# Affichage des résultats
print(resultats)

# Trouver le fichier avec la prédiction de temps d'exécution la plus basse pour chaque taille
# meilleur_fichier <- apply(resultats, 1, function(x) names(x)[which.min(x)])
# print(meilleur_fichier)
# 
# coef1 <- coef(lm(donnees1$temps ~ donnees1$taille))
# 
# intersection_taille <- (coef2[1] - coef1[1]) / (coef1[2] - coef2[2])
# 
# # Afficher la taille d'intersection
# print(paste("Le fichier 2 est meilleur que le fichier 1 à partir de la taille", round(intersection_taille)))








# Transformation logarithmique du temps
donnees1$log_temps <- log(donnees1$temps + 1)  # +1 pour éviter log(0)
donnees2$log_temps <- log(donnees2$temps + 1)

# Régression linéaire avec log_temps
modele1 <- lm(log_temps ~ taille, data = donnees1)
summary(modele1)

modele2 <- lm(log_temps ~ taille, data = donnees2)
summary(modele2)

# Diagramme de dispersion du log(temps d'exécution) en fonction de la taille
plot(donnees2$taille, donnees2$log_temps, col="blue", pch=20, 
     main="Superposition des fichiers 1 et 2", 
     xlab="Taille", ylab="log(Temps d'exécution)", 
     xlim = range(c(donnees1$taille, donnees2$taille)), 
     ylim = range(c(donnees1$log_temps, donnees2$log_temps)))

# Ajout des points pour le fichier 2
points(donnees2$taille, donnees2$log_temps, col="red", pch=17)

# Ajout des lignes de régression
abline(modele1, col="blue")
abline(modele2, col="blue")

# Ajout d'une légende
legend("topleft", legend=c("Fichier 1", "Fichier 2"), col=c("blue", "red"), pch=c(20, 17), bty="n")

# trouver la taille d'intersection
coef1 <- coef(modele1)
coef2 <- coef(modele2)

intersection_taille <- (coef2[1] - coef1[1]) / (coef1[2] - coef2[2])

print(coef1)

# Afficher la taille d'intersection
print(paste("Le fichier 2 est meilleur que le fichier 1 à partir de la taille", round(intersection_taille)))

# affichage d'une ligne verticale pour la taille d'intersection
abline(v=intersection_taille, col="green")
