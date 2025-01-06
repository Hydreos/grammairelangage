import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class LectureFichier {

    static Automate lireFichier(String nomFichier) {
        HashMap<Integer, Etat> etats = new HashMap<>();

        // Lecture du fichier pour savoir combien il y a d'états et leur numéro
        ArrayList<Integer> numeros = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(nomFichier))) {
            String ligne;
            while ((ligne = br.readLine()) != null) {
                if (!ligne.isEmpty() && Character.isDigit(ligne.charAt(0))) {
                    numeros.add(Character.getNumericValue(ligne.charAt(0)));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Création des états, on convertit l'index des états en puissances de 2
        for (Integer numero : numeros) {
            etats.put((int) Math.pow(2, numero), new Etat(numero, 10));
        }

        // Relecture du fichier pour remplir les états avec les transitions
        try (BufferedReader br = new BufferedReader(new FileReader(nomFichier))) {
            String ligne;

            while ((ligne = br.readLine()) != null) {

                // Séparer la ligne en transitions (en utilisant l'espace comme délimiteur)
                // Format d'un état : numero transition transition transition ...
                // Une ligne = un état
                String[] transitions = ligne.split(" ");

                // On récupère le numéro de l'état ainsi que l'état
                int sourceNumero = (int) Math.pow(2, Integer.parseInt(transitions[0]));
                Etat etatSource = etats.get(sourceNumero);

                if (transitions.length > 1) {
                    for (String transition : Arrays.copyOfRange(transitions, 1, transitions.length)) {
                        // Si on lit le mot final, l'état est final
                        if (transition.equals("final")) {
                            etatSource.etatFinal = true;
                            break;
                        }
                        
                        // Format d'une transition : arrivEtat:symbole
                        String[] temp = transition.split(",");
                        etatSource.transitions.add(new Transition(Integer.parseInt(temp[0]), 10, temp[1]));
                    }
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        // Construction de l'automate
        Automate automate = new Automate();
        for (Integer index : etats.keySet()) {
            automate.ajoutEtat(etats.get(index));
        }

        // Remplissage de la liste des symboles
        automate.remplirSymboles();

        return automate;
    }
}
