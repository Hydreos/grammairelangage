import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Automate {

    Etat initial;

    static HashMap<Integer, Etat> etats = new HashMap<>();

    Automate(Etat e) {
        this.initial = e;
    }

    void ajoutEtat(Etat e) {
        etats.put(e.exp, e);
    }

    static void conversionAfnAfd(Etat e, ArrayList<Etat> visites) {

        // L'état courant est visité (pour eviter les boucles infinies)
        visites.add(e);

        // On liste les états qui constituent l'éventuel super-état courant
        ArrayList<Etat> sousEtats = new ArrayList<>();
        for (Integer i : decomposerEnPuissancesDe2(e.exp)) {
            sousEtats.add(etats.get(i));
        }

        // On regroupe les transitions qui lisent le même symbole
        HashMap<String, ArrayList<Transition>> symbolesLus = new HashMap<>();

        for (Etat sousEtat : sousEtats) {
            for (Transition t : sousEtat.transitions) {
                if (symbolesLus.containsKey(t.symbole)) {
                    symbolesLus.get(t.symbole).add(t);
                } else {
                    symbolesLus.put(t.symbole, new ArrayList<>());
                    symbolesLus.get(t.symbole).add(t);
                }
            }
        }

        // On crée une liste qui contiendra les transitions analysées
        ArrayList<Transition> temp_transitions = new ArrayList<>();

        // Pour chaque symbole, on crée si besoin un super-état avec chaque état
        // d'arrivé avec ce symbole
        for (String symbole : symbolesLus.keySet()) {

            ArrayList<Transition> transitions = symbolesLus.get(symbole);
            int somme = 0;

            System.out.println("\nEtat : " + e.exp + ", Symbole : " + symbole);
            System.out.println("Somme");

            // Pour créer le numéro du super-état, on applique un OU bit à bit sur le numéro
            // des états qui le crée
            for (Transition t : transitions) {
                System.out.println(t.arrivEtat.exp);
                somme = somme | t.arrivEtat.exp;
            }

            // Si l'état est déjà visité, on ne crée pas de nouvel état et on lie l'état actuel avec une
            // transition du symbole courant vers l'état visité
            if (visites.contains(etats.get(somme))) {
                System.out.println("Etat " + somme + " déjà visité");
                temp_transitions.add(new Transition(etats.get(somme), symbole));
            }
            // Sinon on crée un super-état et on applique la fonction récursive à ce nouvel
            // état
            else {
                System.out.println("Nouvel etat : " + somme + ", Binaire : " + Integer.toBinaryString(somme));
                Etat superEtat = new Etat(somme, 2);
                temp_transitions.add(new Transition(superEtat, symbole));
                etats.put(somme, superEtat);
                conversionAfnAfd(superEtat, visites);
            }

            System.out.println(" ");
        }

        // On remplace les anciennes transitions de l'état de l'AFN par les nouvelles de l'AFD
        e.transitions = temp_transitions;
    }

    static List<Integer> decomposerEnPuissancesDe2(int nombre) {
        List<Integer> result = new ArrayList<>();
        int puissance = 1;

        while (nombre > 0) {
            if ((nombre & 1) == 1) { // Vérifie si le bit de poids faible est 1
                result.add(puissance); // Ajoute la puissance de 2 actuelle
            }
            nombre >>= 1; // Décale les bits de nombre vers la droite
            puissance *= 2; // Passe à la puissance de 2 suivante
        }

        return result;
    }
}