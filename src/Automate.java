import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

public class Automate {

    // Les états sont indexés par leur combinaison de puissances de 2
    HashMap<Integer, Etat> etats = new HashMap<>();
    List<String> symboles = new ArrayList<>();

    void ajoutEtat(Etat e) {
        etats.put(e.exp, e);
    }

    Etat getEtat(int i) {
        return etats.get(i);
    }

    // Remplissage de la liste des symboles de l'automate
    void remplirSymboles() {
        for (Etat etat : etats.values()) {
            for (Transition transition : etat.transitions) {
                if (!symboles.contains(transition.symbole))
                    symboles.add(transition.symbole);
            }
        }
    }

    static Automate conversionAfnAfd(Etat initial, Automate afn) {
        Automate afd = new Automate();
        afd.conversionAfnAfd_recursion(initial, afn);
        afd.remplirSymboles();
        return afd;
    }

    void conversionAfnAfd_recursion(Etat e, Automate afn) {
        // On copie l'état courant pour ne pas altérer l'original
        e = new Etat(e);

        // L'état courant est visité afin d'éviter les boucles infinies
        // (visité = dans la liste des états du nouvel automate)
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

        // On crée une liste qui contiendra les nouvelles transitions
        ArrayList<Transition> temp_transitions = new ArrayList<>();

        // Pour chaque symbole, on crée si besoin un super-état qui est composé de
        // chaque état d'arrivé du symbole courant
        for (String symbole : symbolesLus.keySet()) {

            ArrayList<Transition> transitions = symbolesLus.get(symbole);
            int somme = 0;
            boolean isFinal = false;

            // Pour créer le numéro du super-état, on applique un OU bit à bit sur le numéro
            // des états qui le crée
            for (Transition t : transitions) {
                if (afn.etats.get(t.arrivEtat).etatFinal)
                    isFinal = true;

                somme = somme | t.arrivEtat;
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
                superEtat.etatFinal = isFinal;
                temp_transitions.add(new Transition(somme, 2, symbole));
                conversionAfnAfd_recursion(superEtat, afn);
            }
        }

        // On ajoute les nouvelles transitions à l'état courant
        e.transitions = temp_transitions;
    }

    static Automate minimisationAfd(Automate afd) {
        Automate afdMin = new Automate();

        // Les états des couples sont stockés dans un HashMap
        HashMap<Pair<Etat>, Boolean> couples = new HashMap<>();

        // Toutes les combinaisons des états sont ajoutées au HashMap
        for (Etat e1 : afd.etats.values()) {
            for (Etat e2 : afd.etats.values()) {
                // On veut exclusivement des couples d'état différents
                if (e1.exp != e2.exp)
                    // Les couples ne sont pas marqués à leur création
                    couples.put(new Pair<Etat>(e1, e2), false);
            }
        }

        // On cherche les états finaux de l'automate
        List<Etat> etatsFinaux = new ArrayList<>();
        for (Etat etat : afd.etats.values()) {
            if (etat.etatFinal)
                etatsFinaux.add(etat);
        }

        // On marque les premiers couples
        for (Etat etatFinal : etatsFinaux) {
            for (Etat etat : afd.etats.values()) {
                if (etat.etatFinal)
                    continue;
                Pair<Etat> tempCouple = new Pair<Etat>(etatFinal, etat);
                couples.put(tempCouple, true);
            }
        }

        // Tant qu'on marque de nouveaux couples on parcourt chaque état non marqué
        Boolean marque = true;
        while (marque) {
            marque = false;
            for (Map.Entry<Pair<Etat>, Boolean> couple : couples.entrySet()) {
                // Si le couple n'est pas marqué
                if (couple.getValue() == false) {
                    Etat e1 = couple.getKey().first;
                    Etat e2 = couple.getKey().second;

                    for (String symbole : afd.symboles) {
                        // On cherche les transitions avec le symbole courant
                        Optional<Transition> transition1 = e1.transitions.stream()
                                .filter(transition -> transition.symbole.equals(symbole))
                                .findFirst();
                        // On récupère l'état d'arrivée de la transition
                        int temp_e1_exp = transition1.get().arrivEtat;
                        Etat temp_e1 = afd.etats.get(temp_e1_exp);

                        // On cherche les transitions avec le symbole courant
                        Optional<Transition> transition2 = e2.transitions.stream()
                                .filter(transition -> transition.symbole.equals(symbole))
                                .findFirst();
                        // On récupère l'état d'arrivée de la transition
                        int temp_e2_exp = transition2.get().arrivEtat;
                        Etat temp_e2 = afd.etats.get(temp_e2_exp);

                        // Si le couple d'arrivé est marqué, on marque le couple courant
                        if (couples.containsKey(new Pair<Etat>(temp_e1, temp_e2))) {
                            if (couples.get(new Pair<Etat>(temp_e1, temp_e2)) == true) {
                                couples.put(couple.getKey(), true);
                                marque = true;
                            }
                        }
                    }
                }
            }
        }

        // Création des groupes d'états à fusionner
        Map<Etat, Set<Etat>> groupes = new HashMap<>();

        for (Map.Entry<Pair<Etat>, Boolean> couple : couples.entrySet()) {
            if (!couple.getValue()) {
                Etat e1 = couple.getKey().first;
                Etat e2 = couple.getKey().second;

                // Obtenir ou créer les groupes
                Set<Etat> groupe1 = groupes.computeIfAbsent(e1, k -> new HashSet<>(Arrays.asList(e1)));
                Set<Etat> groupe2 = groupes.computeIfAbsent(e2, k -> new HashSet<>(Arrays.asList(e2)));

                // Fusionner les groupes
                Set<Etat> groupeFusionne = new HashSet<>();
                groupeFusionne.addAll(groupe1);
                groupeFusionne.addAll(groupe2);

                // Mettre à jour les références pour tous les états du groupe
                for (Etat e : groupeFusionne) {
                    groupes.put(e, groupeFusionne);
                }
            }
        }

        // Fusionner les états par groupe
        Set<Set<Etat>> groupesUniques = new HashSet<>(groupes.values());
        List<Etat> etatsFusionnes = new ArrayList<>();

        for (Set<Etat> groupe : groupesUniques) {
            // Créer le nouvel état fusionné
            Etat etatFusionne = new Etat(0, 2);
            boolean estFinal = false;
            boolean estInitial = false;

            // Combiner les propriétés de tous les états du groupe
            Map<String, Integer> transitionsParSymbole = new HashMap<>();
            for (Etat e : groupe) {
                etatFusionne.exp |= e.exp;
                estFinal |= e.etatFinal;
                estInitial |= e.etatInitial;

                // Regrouper les transitions par symbole et additionner les états d'arrivée
                for (Transition t : e.transitions) {
                    transitionsParSymbole.merge(t.symbole, t.arrivEtat, Integer::sum);
                }
            }

            // Créer les nouvelles transitions fusionnées
            for (Map.Entry<String, Integer> entry : transitionsParSymbole.entrySet()) {
                etatFusionne.transitions.add(new Transition(entry.getValue(), 2, entry.getKey()));
            }

            etatFusionne.etatFinal = estFinal;
            etatFusionne.etatInitial = estInitial;
            etatsFusionnes.add(etatFusionne);
        }

        // Après la boucle de fusion des états
        afdMin.symboles = new ArrayList<>(afd.symboles);

        // Ajouter les états fusionnés
        for (Etat etatFusionne : etatsFusionnes) {
            afdMin.etats.put(etatFusionne.exp, etatFusionne);
        }

        // Identifier et ajouter les états non fusionnés
        Set<Etat> etatsInclusDansGroupes = new HashSet<>();
        for (Set<Etat> groupe : groupesUniques) {
            etatsInclusDansGroupes.addAll(groupe);
        }

        for (Etat etat : afd.etats.values()) {
            if (!etatsInclusDansGroupes.contains(etat)) {
                // Créer une copie de l'état
                Etat nouvEtat = new Etat(etat);
                afdMin.etats.put(etat.exp, nouvEtat);
            }
        }

        return afdMin;
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
        System.out.print("Symboles :");
        for (String symbole : symboles) {
            System.out.print(" " + symbole);
        }

        System.out.print("\n");
        for (Integer i : etats.keySet()) {

            String stringFinal = "";
            if (etats.get(i).etatFinal)
                stringFinal = " final";
            System.out.println("Etat" + stringFinal + " : " + i);

            for (Transition t : etats.get(i).transitions) {
                System.out.println(t.symbole + ", " + t.arrivEtat);
            }
        }
    }
}