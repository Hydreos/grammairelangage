import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Automate {

    HashMap<Integer, Etat> etats = new HashMap<>();

    void ajoutEtat(Etat e) {
        etats.put(e.exp, e);
    }

    Etat getEtat(int i) {
        return etats.get(i);
    }

    void conversionAfnAfd(Etat e, Automate afn) {

        // L'état courant est visité (pour eviter les boucles infinies)
        ajoutEtat(e);

        // On liste les états qui constituent l'éventuel super-état courant
        ArrayList<Etat> sousEtats = new ArrayList<>();
        for (Integer i : decomposerEnPuissancesDe2(e.exp)) {
            sousEtats.add(afn.etats.get(i));
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

        // Pour chaque symbole, on crée si besoin un super-état qui est composé de
        // chaque état d'arrivé du symbole courant
        for (String symbole : symbolesLus.keySet()) {

            ArrayList<Transition> transitions = symbolesLus.get(symbole);
            int somme = 0;

            // Pour créer le numéro du super-état, on applique un OU bit à bit sur le numéro
            // des états qui le crée
            for (Transition t : transitions) {
                somme = somme | afn.getEtat(t.arrivEtat).exp;
            }

            // Si l'état vers lequel on veut aller est déjà visité, on ne crée pas de nouvel
            // état et on lie l'état actuel avec une transition du symbole courant vers
            // l'état visité (somme = l'état vers où on veut aller)
            if (etats.containsKey(somme)) {
                temp_transitions.add(new Transition(somme, 2, symbole));
            }
            // Sinon on crée un super-état et on applique la fonction récursive à ce nouvel
            // état et on lie l'état actuel avec une transition du symbole courant vers le
            // nouvel état
            else {
                Etat superEtat = new Etat(somme, 2);
                temp_transitions.add(new Transition(somme,2, symbole));
                conversionAfnAfd(superEtat, afn);
            }
        }

        // On ajoute les nouvelles transitions à l'état courant
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

    void affiche() {
        for (Integer i : etats.keySet()) {
            System.out.println("Etat : " + i);
            for (Transition t : etats.get(i).transitions) {
                System.out.println(t.symbole + ", " + t.arrivEtat);
            }
        }
    }
}