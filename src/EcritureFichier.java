import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class EcritureFichier {

    static void ecrireAutomate(String nomFichier, Automate automate) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(nomFichier))) {

            // Parcourir les états de l'automate
            for (Etat etat : automate.etats.values()) {
                // Écrire le numéro de l'état
                bw.write(etat.exp + "");

                // Ajouter les transitions
                for (Transition transition : etat.transitions) {
                    bw.write(" " + transition.arrivEtat + "," + transition.symbole);
                }

                // Précise si l'état est final
                if(etat.etatFinal) bw.write(" final");

                // Passer à la ligne suivante
                bw.newLine();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}