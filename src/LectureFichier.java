import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class LectureFichier {

    static Automate lireFichier(String nomFichier)
    {
        List<Etat> etats = new ArrayList<>();

        // Lecture du fichier pour savoir combien il y a d'états
        int lineCount = 0;
        try (BufferedReader br = new BufferedReader(new FileReader(nomFichier))) {
            while (br.readLine() != null) {
                lineCount++;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        for (int index = 0; index < lineCount; index++) {
            etats.add(new Etat(index, 10));
        }

        // Relecture du fichier pour remplir les états avec les transitions
        try (BufferedReader br = new BufferedReader(new FileReader(nomFichier))) {
            String ligne;
            int compteur = 0;

            while ((ligne = br.readLine()) != null) {
                
                // Séparer la ligne en transitions (en utilisant l'espace comme délimiteur)
                // Format d'un état : transition transition transition ...
                // Une ligne = un état
                // 1ere ligne = état 0, 2eme ligne = état 1, 3eme ligne = état 2 etc...
                String[] transitions = ligne.split(" ");
                for (String transition : transitions) {
                    // Format d'une transition : symbole:arrivEtat
                    String[] temp = transition.split(",");
                    etats.get(compteur).transitions.add(new Transition(etats.get(Integer.parseInt(temp[0])), temp[1]));
                }
            }
            
        } catch (IOException e) {
            e.printStackTrace();
        }

        Automate afn = new Automate(null)

        return afn;
    }
}
