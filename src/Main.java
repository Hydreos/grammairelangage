import java.util.ArrayList;

public class Main {
    public static void main(String[] args) throws Exception {
        Etat trois = new Etat(3, 10);
        Etat deux = new Etat(2, 10);
        Etat un = new Etat(1, 10);
        Etat zero = new Etat(0, 10);

        deux.transitions.add(new Transition(trois, "b"));
        un.transitions.add(new Transition(deux, "a"));
        zero.transitions.add(new Transition(un, "a"));
        zero.transitions.add(new Transition(zero, "a"));
        zero.transitions.add(new Transition(zero, "b"));

        Automate afn = new Automate(zero);
        afn.ajoutEtat(zero);
        afn.ajoutEtat(un);
        afn.ajoutEtat(deux);
        afn.ajoutEtat(trois);

        System.out.println("\nAFN");
        for (Integer i : Automate.etats.keySet()) {
            System.out.println(i);
        }
        System.out.println("");
        
        Automate.conversionAfnAfd(zero, new ArrayList<>());
         
        for (Integer i : Automate.etats.keySet()) {
            System.out.println("Etat : "+i);
            for (Transition t : Automate.etats.get(i).transitions) {
                System.out.println(t.symbole+", "+t.arrivEtat.exp);
            }
        }
    }
}