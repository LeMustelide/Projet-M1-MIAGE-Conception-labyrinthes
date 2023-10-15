import subprocess
import time
import csv
import random
from concurrent.futures import ProcessPoolExecutor
import os

commande = './v2/bin/labyrinthe'

def execute_program(taille):
    debut = time.time()
    commande_complete = commande + ' ' + str(taille)
    result = subprocess.run(commande_complete, stdout=subprocess.PIPE, stderr=subprocess.PIPE, text=True, shell=True)
    fin = time.time()
    if(result.returncode != 0):
        temps_execution = -1
    else:
        temps_execution = fin - debut
    return taille, temps_execution, result

def write_results(resultats):
    # Vérifie si le fichier existe déjà
    file_exists = os.path.isfile('resultatsV2.csv')
    with open('resultatsV2.csv', mode='a', newline='') as file:
        writer = csv.writer(file)
        # Si le fichier n'existe pas, écrivez l'en-tête
        if not file_exists:
            writer.writerow(['taille', 'temps'])
        writer.writerows(resultats)
tailles = [random.randint(10, 5000) for _ in range(20)]

with ProcessPoolExecutor(max_workers=2) as executor:
    resultats = list(executor.map(execute_program, tailles))

somme = sum(temps for _, temps, _ in resultats)
print(f'Temps d\'exécution moyenne : {somme / len(resultats)} secondes')

for taille, temps, result in resultats:
    if result.returncode != 0:
        print(f'Erreur pour la taille {taille} : {result.stderr}')
    else:
        print(f'Sortie pour la taille {taille} : {result.stdout}')

write_results([(taille, temps) for taille, temps, _ in resultats])
